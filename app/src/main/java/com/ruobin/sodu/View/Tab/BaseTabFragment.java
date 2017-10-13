package com.ruobin.sodu.View.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ruobin.sodu.DBHelper.BookCacheDao;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.R;
import com.ruobin.sodu.SearchActivity;
import com.ruobin.sodu.Util.CustomRecyclerAdapter;
import com.ruobin.sodu.Util.DividerItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruobin on 2017/9/26.
 */
public abstract class BaseTabFragment extends Fragment {

    private boolean isVisible;

    public boolean isLoading;

    public List<Book> books;

    protected View currentView;


    private RecyclerView mRecyclerView;

    public RefreshLayout refreshLayout;

    private CustomRecyclerAdapter<Book> mAdapter;

    private int tabId;
    private int listItemId;

    private boolean isNeeLoadMore;

    BookCacheDao.BookCacheType cacheType = BookCacheDao.BookCacheType.None;

    BookCacheDao dao;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    updateUI();
                    break;
                default:
                    break;
            }
        }

    };

    public void setId(int tab, int listItem, Boolean loadMore, BookCacheDao.BookCacheType cache) {
        tabId = tab;
        listItemId = listItem;
        isNeeLoadMore = loadMore;
        cacheType = cache;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (currentView == null) {

            currentView = inflater.inflate(tabId, container, false);
            //在这里做一些初始化处理
            initRefreshView();
            initRecylerView();
            initUI();

        } else {

            ViewGroup viewGroup = (ViewGroup) currentView.getParent();

            if (viewGroup != null)
                viewGroup.removeView(currentView);
        }

        if (isVisible && ifNeedLoadData() && !isLoading) {
            if (refreshLayout != null) {
                refreshLayout.autoRefresh();
            }
        }

        dao = new BookCacheDao(getContext());

        return currentView;
    }

    public void loadCacheData() {

//        if(cacheType != BookCacheDao.BookCacheType.OnlineShelf){
//            return;
//        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Book> caches = dao.getBooksByCacheType(cacheType);
                    if(caches != null && caches.size() >0) {
                        books = caches;
                        mHandler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void initUI() {

        ImageButton btnSearch = (ImageButton) currentView.findViewById(R.id.btn_search);
        if (btnSearch == null) {
            return;
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                //设置Intent的class属性，跳转到SecondActivity
                intent.setClass(currentView.getContext(), SearchActivity.class);
                //启动Activity
                startActivity(intent);
            }
        });

    }

    protected void initRefreshView() {

        refreshLayout = (RefreshLayout) currentView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadmore(isNeeLoadMore);

//        //设置 Header 为 Material风格
//        refreshLayout.setRefreshHeader(new ClassicsHeader(this.getContext()));
//        //设置 Footer 为 球脉冲
//        refreshLayout.setRefreshFooter(new ClassicsFooter(this.getContext()));

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

        mRecyclerView = (RecyclerView) currentView.findViewById(R.id.list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.setAdapter(mAdapter = new CustomRecyclerAdapter(books, listItemId));
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisibleToUser && ifNeedLoadData() && !isLoading) {
            if (refreshLayout != null) {
                refreshLayout.autoRefresh();
            }
        }
    }


    public boolean ifNeedLoadData() {

        if (books == null || books.size() == 0) {
            return true;
        }
        return false;
    }

    //设置页面数据方法
    public void setData(String html) {


    }


    public void updateUI() {
        if (books != null && books.size() > 0) {
            mAdapter.updateData(books);
            dao.addBooks(books, cacheType);
        }
    }


    public void endLoad() {

        refreshLayout.finishRefresh(0);
        refreshLayout.finishLoadmore(0);
    }


    public final void scrollToTop() {

        if (books != null && books.size() > 7) {
            if (mRecyclerView.computeVerticalScrollOffset() > 0) {
                mRecyclerView.scrollToPosition(6);
                mRecyclerView.smoothScrollToPosition(0);
            }
        }
    }

    //获取数据失败时
    public void onRequestFailure() {

        if (books == null || books.size() == 0) {
            setErrorViewVisibility(true);
        }

        endLoad();
    }


    //当fragment隐藏时
    public void onFragmentUnVisible() {
    }


    //当fragment显示时
    public void onFragmentVisible() {


    }

    //获取数据
    public void loadData() {
        isLoading = true;
        setErrorViewVisibility(false);
        if(books==null || books.size() ==0) {
            loadCacheData();
        }
    }

    //获取数据
    public void loadMoreData() {

    }


    public void setErrorViewVisibility(boolean isVisible) {

        if (isVisible) {
            this.currentView.findViewById(R.id.refresh_error).setVisibility(View.VISIBLE);
        } else {

            this.currentView.findViewById(R.id.refresh_error).setVisibility(View.GONE);
        }

    }

    //点击
    public abstract void itemClick(View view, int position);

    //长按
    public abstract void itemLongClick(View view, int position);

    //初始化列表项数据
    public abstract void itemInitData(View view, Object item);
}
