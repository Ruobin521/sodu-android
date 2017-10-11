package com.ruobin.sodu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Util.CustomRecyclerAdapter;
import com.ruobin.sodu.Util.DividerItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListViewActivity extends Activity {

    public List<Book> books = new ArrayList<Book>();

    private RecyclerView mRecyclerView;

    public RefreshLayout refreshLayout;

    private CustomRecyclerAdapter<Book> mAdapter;

    public int  layoutId ;

    public int  itemLayoutId ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);

        initRecylerView();
        initRefreshView();
        initView();
    }

    @CallSuper
    public  void updateData(String html) {
        mAdapter.updateData(books);
    }

    protected void initRefreshView() {
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
    }

    protected void initRecylerView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new CustomRecyclerAdapter(books, itemLayoutId));
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

    public abstract void initView();

    public abstract void loadData();

    public abstract void onRequestFailure();

    public abstract void itemClick(View view, int position);


    public abstract void itemLongClick(View view, int position);


    public abstract void itemInitData(View view, Object item) ;


}
