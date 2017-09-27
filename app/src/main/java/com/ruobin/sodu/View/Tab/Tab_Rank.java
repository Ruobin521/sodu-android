package com.ruobin.sodu.View.Tab;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Util.CustomRecyclerAdapter;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Util.DividerItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;


public class Tab_Rank extends BaseTabFragment {

    private int pageIndex = 0;
    private int pageCount = 8;

    private RecyclerView mRecyclerView;
    private CustomRecyclerAdapter<Book> mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (currentView == null) {
            currentView = inflater.inflate(R.layout.fragment_tab_rank, container, false);
            //在这里做一些初始化处理
            initRefreshView();
            initRecylerView();

        } else {
            ViewGroup viewGroup = (ViewGroup) currentView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(currentView);
        }
        return currentView;
    }


    private void initRefreshView() {

        RefreshLayout refreshLayout = (RefreshLayout) currentView.findViewById(R.id.refreshLayout);
        //设置 Header 为 Material风格
        refreshLayout.setRefreshHeader(new ClassicsHeader(this.getContext()));
       //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new ClassicsFooter(this.getContext()));


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
                Toast.makeText(currentView.getContext(),"下来刷新",Toast.LENGTH_SHORT).show();
            }
        });


        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);

                Toast.makeText(currentView.getContext(),"上拉加载",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initRecylerView() {

        mRecyclerView = (RecyclerView) currentView.findViewById(R.id.rank_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(mAdapter = new CustomRecyclerAdapter(books));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),
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

    @Override
    public void onFragmentVisible() {

        if (!ifNeedLoadData()) {
            return;
        }

        String rankUrl = SoDuUrl.rank;
        loadData(rankUrl);
    }


    @Override
    public void loadData(String url) {

        getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {
                setData(html);
            }

            @Override
            public void error() {
                onRequestFailure();
            }
        });

    }

    @Override
    public boolean ifNeedLoadData() {

        if (books == null || books.size() == 0) {
            return true;
        }

        return false;
    }

    @Override
    public void setData(String html) {

        books = new ArrayList<Book>();
        for (int i = 'A'; i < 'z'; i++) {

            Book b = new Book();
            b.BookName = "" + (char) i + 1111;
            books.add(b);
        }

        mAdapter.updateData(books);
    }


    @Override
    public void itemClick(View view, int position) {
        Toast.makeText(getActivity(), "click " + position + " item", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemLongClick(View view, int position) {

        Toast.makeText(getActivity(), "long click " + position + " item", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void itemInitData(View view, Object item) {

        TextView tv = (TextView) view.findViewById(R.id.item_rank_txt);
        tv.setText(((Book) item).BookName);

    }


    @Override
    public void onRequestFailure() {


    }

    @Override
    public void onFragmentUnVisible() {

    }

}
