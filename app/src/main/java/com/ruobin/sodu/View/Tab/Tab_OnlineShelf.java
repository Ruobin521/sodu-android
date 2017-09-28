package com.ruobin.sodu.View.Tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.R;


public class Tab_OnlineShelf extends BaseTabFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setId(R.layout.fragment_tab_online_shelf,R.layout.item_rank);
    }


    public void loadData() {
        super.loadData();

        String url = SoDuUrl.bookShelfPage;

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
    public void setData(String html) {
        super.setData(html);
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
