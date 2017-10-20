package com.ruobin.sodu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.Util.CustomRecyclerAdapter;
import com.ruobin.sodu.Util.DividerItemDecoration;
import com.ruobin.sodu.Util.HttpHelper;
import com.ruobin.sodu.View.PageReader.PageReaderActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateCatalogActivity extends Activity {

    public List<Book> books = new ArrayList<Book>();
    Book currentBook;
    private int pageIndex = 0;
    private int pageCount = 0;

    private RecyclerView mRecyclerView;

    public RefreshLayout refreshLayout;

    private CustomRecyclerAdapter<Book> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_catalog);

        Intent intent = getIntent();
        Book book = (Book) intent.getSerializableExtra("book");

        currentBook = book;
        TextView textView = (TextView) findViewById(R.id.header_title);
        textView.setText(book.BookName);

        initRefreshView();
        initRecylerView();
        initView();

        refreshLayout.autoRefresh();

    }

    private  void initView() {

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.navigation_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    protected void initRefreshView() {

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadData();
                //Toast.makeText(currentView.getContext(), "下拉刷新", Toast.LENGTH_SHORT).show();
                //  refreshlayout.finishRefresh(30);
            }
        });


        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                loadMoreData();
                //   Toast.makeText(currentView.getContext(), "上拉加载", Toast.LENGTH_SHORT).show();
            }
        });


    }

    protected void initRecylerView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new CustomRecyclerAdapter(books, R.layout.item_update_catalog));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new CustomRecyclerAdapter.ItemActionListener() {
            @Override
            public void onItemClick(View view, int position) {
                itemClick(view, position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                itemLongClick(view, position);
            }

            @Override
            public void onItemInitData(View view, Object item) {
                itemInitData(view, item);
            }
        });
    }


    public void loadData() {

        loadDataByIndex(1);
    }

    public void loadMoreData() {

        if (pageIndex >= pageCount) {
            endLoad();
            return;
        } else {
            loadDataByIndex(pageIndex + 1);
        }
    }

    private void loadDataByIndex(int index) {
        setErrorViewVisibility(false);
        String url = "";
        if (index == 1) {
            url = currentBook.SoDuUpdateCatalogUrl;
        } else {
            url = currentBook.SoDuUpdateCatalogUrl.replace(".html","_" + index+".html");
        }

        pageIndex = index;
        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {
                setData(html);
            }

            @Override
            public void error() {
                onRequestFailure();
            }

            @Override
            public void end() {

            }
        });
    }

    public void setData(String html) {

        if (html == null || html == "") {
            return;
        }
        List<Book> list = ListDataAnalysisService.getUpdateCatalogList(html, currentBook.BookId, currentBook.BookName);
        if (list == null) {
            return;
        }
        if (books == null) {
            books = new ArrayList<Book>();
        }

        if (pageIndex == 1) {
            pageCount = ListDataAnalysisService.getUpdateCatalogPageCount(html);
            books.clear();

        }
        books.addAll(list);
        this.updateData();
    }


    public void updateData() {
        TextView tx = (TextView) findViewById(R.id.header_title);

        StringBuilder stringBuilder = new StringBuilder(currentBook.BookName)
                .append("-")
                .append(pageIndex)
                .append("/")
                .append(pageCount);

        tx.setText(stringBuilder.toString());
        mAdapter.updateData(books);
        endLoad();

    }


    //获取数据失败时
    public void onRequestFailure() {

        if (books == null || books.size() == 0) {
            setErrorViewVisibility(true);
        }
        endLoad();
    }

    private  void setErrorViewVisibility(boolean isVisiable) {
        this.findViewById(R.id.refresh_error).setVisibility(isVisiable? View.VISIBLE :View.GONE);
    }

    private void endLoad() {

        refreshLayout.finishRefresh(0);
        refreshLayout.finishLoadmore(0);
    }

    public void itemClick(View view, int position) {
        Book book = books.get(position);
        Intent intent = new Intent();
        intent.setClass(this, PageReaderActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }


    public void itemLongClick(View view, int position) {
       // Toast.makeText(this, "long click " + position + " item", Toast.LENGTH_SHORT).show();
        return;
    }


    public void itemInitData(View view, Object item) {

        TextView tv = (TextView) view.findViewById(R.id.item_update_catalog_name);
        tv.setText(((Book) item).NewestCatalogName);

        TextView tv2 = (TextView) view.findViewById(R.id.item_update_catalog_lywz);
        tv2.setText(((Book) item).LyWeb);

        TextView tv3 = (TextView) view.findViewById(R.id.item_catalog_update_time);
        tv3.setText(((Book) item).UpdateTime);
    }


}
