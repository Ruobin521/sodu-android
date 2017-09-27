package com.ruobin.sodu.View.Tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruobin.sodu.R;
import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;


public class Tab_OnlineShelf extends BaseTabFragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aa===","Tab_OnlineShelf销毁");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (currentView == null) {
            currentView = inflater.inflate(R.layout.fragment_tab_online_shelf, container, false);
            //在这里做一些初始化处理

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

        String url = SoDuUrl.bookShelfPage;
        loadData(url);
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
    public void setData(String html) {

        TextView txt = (TextView) this.getView().findViewById(R.id.txt_rank_content_online_shelft);
        txt.setText(html);
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
