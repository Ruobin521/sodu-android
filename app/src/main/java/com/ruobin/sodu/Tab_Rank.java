package com.ruobin.sodu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.URL.SoDuUrl;

import java.util.ArrayList;
import java.util.List;


public class Tab_Rank extends BaseTabFragment {

    private int pageIndex = 0;
    private int pageCount = 8;

    private RecyclerView mRecyclerView;
    private CustomRecyclerAdapter<String> mAdapter;
    private List<String> mDatas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (currentView == null) {
            currentView = inflater.inflate(R.layout.fragment_tab_rank, container, false);
            //在这里做一些初始化处理

            mRecyclerView = (RecyclerView) currentView.findViewById(R.id.rank_recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            mRecyclerView.setAdapter(mAdapter = new CustomRecyclerAdapter(mDatas));

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
        } else {
            ViewGroup viewGroup = (ViewGroup) currentView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(currentView);
        }
        return currentView;
    }


    @Override
    public void onFragmentVisible() {

        if (!ifNeedLoadData()) {
            return;
        }

        String rankUrl = SoDuUrl.rank;
        initData();
        mAdapter.updateData(mDatas);
    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }


    @Override
    public boolean ifNeedLoadData() {

        if (mDatas == null || mDatas.size() == 0) {
            return true;
        }

        return  false;
    }

    @Override
    public void setData(String html) {


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
        tv.setText((String) item);

    }


    @Override
    public void onRequestFailure() {


    }

    @Override
    public void onFragmentUnVisible() {

    }

}
