package com.ruobin.sodu.View.setting;

import android.view.View;
import android.widget.LinearLayout;

import com.ruobin.sodu.BaseListViewActivity;
import com.ruobin.sodu.R;

public class DownloadCenterActivity extends BaseListViewActivity {


    public  DownloadCenterActivity() {

        layoutId = R.layout.activity_download_center;

    }

    @Override
    public void initView() {

        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.navigation_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void loadData() {

    }

    @Override
    public void onRequestFailure() {

    }

    @Override
    public void itemClick(View view, int position) {

    }

    @Override
    public void itemLongClick(View view, int position) {

    }

    @Override
    public void itemInitData(View view, Object item) {

    }
}
