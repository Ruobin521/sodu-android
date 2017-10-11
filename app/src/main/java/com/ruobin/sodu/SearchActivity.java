package com.ruobin.sodu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.Util.CustomRecyclerAdapter;
import com.ruobin.sodu.Util.DividerItemDecoration;
import com.ruobin.sodu.Util.HttpHelper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public List<Book> books = new ArrayList<Book>();

    private RecyclerView mRecyclerView;

    public RefreshLayout refreshLayout;

    private CustomRecyclerAdapter<Book> mAdapter;

    LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initRefreshView();
        initRecylerView();
        initView();
    }


    private void initView() {

        loading = (LinearLayout) findViewById(R.id.loading);

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.navigation_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSearch();
            }
        });


        EditText text = (EditText) findViewById(R.id.txt_search);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    onSearch();
                }
                return false;
            }
        });

    }

    protected void initRefreshView() {
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
    }

    protected void initRecylerView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new CustomRecyclerAdapter(books, R.layout.item_rank));
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


    public void onSearch() {
        try {
            EditText text = (EditText) findViewById(R.id.txt_search);

            if (TextUtils.isEmpty(text.getText().toString())) {
                Toast.makeText(this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                return;
            }
            String searchPara = text.getText().toString();
            searchPara = URLEncoder.encode(searchPara, "UTF-8");
            //Toast.makeText(this,searchPara,Toast.LENGTH_SHORT).show();
            search(searchPara);

        } catch (Exception ex) {
        }
    }


    private void search(String para) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }

        loading.setVisibility(View.VISIBLE);

        String url = SoDuUrl.bookSearchPage + para;

        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {
                loading.setVisibility(View.GONE);
                setData(html);
            }

            @Override
            public void error() {
                loading.setVisibility(View.GONE);
                onRequestFailure();
            }
        });

    }

    public void setData(String html) {

        books.clear();

        if (html == null || html == "") {
            return;
        }
        List<Book> list = ListDataAnalysisService.AnalysisRankDatas(html);
        if (list == null) {
            return;
        }
        if (books == null) {
            books = new ArrayList<Book>();
        }
        books.addAll(list);
        this.updateData();
    }


    public void updateData() {
        mAdapter.updateData(books);
    }

    public void onRequestFailure() {


    }


    public void itemClick(View view, int position) {

        Book book = books.get(position);
        Intent intent = new Intent();
        intent.setClass(this, UpdateCatalogActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);

    }


    public void itemLongClick(View view, int position) {
        Toast.makeText(this, "long click " + position + " item", Toast.LENGTH_SHORT).show();
    }


    public void itemInitData(View view, Object item) {

        TextView tv = (TextView) view.findViewById(R.id.item_rank_name);
        tv.setText(((Book) item).BookName);

        TextView tv2 = (TextView) view.findViewById(R.id.item_rank_catalog_name);
        tv2.setText(((Book) item).NewestCatalogName);

        TextView tv3 = (TextView) view.findViewById(R.id.item_rank_update_time);
        tv3.setText(((Book) item).UpdateTime);
    }


}
