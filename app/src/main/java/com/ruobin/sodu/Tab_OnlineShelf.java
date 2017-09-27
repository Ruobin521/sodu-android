package com.ruobin.sodu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruobin.sodu.URL.SoDuUrl;
import com.ruobin.sodu.Util.HttpHelper;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Tab_OnlineShelf extends BaseTabFragment {


    private  boolean isLoaded = false;
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

        if(isLoaded) {
            return;
        }
//        if (!ifNeedLoadData()) {
//            return;
//        }
//
//        String rankUrl = SoDuUrl.home;
//        loadData(rankUrl);

        String url = SoDuUrl.loginPostPage;

        //  var postdata = "username=" + this.UserName + "&userpass=" + this.PassWd;

        Map<String, String> map=new HashMap<>();
        map.put("username","918201");
        map.put("userpass","8166450");

        HttpHelper.postMethod(url, map, new Callback<ResponseBody>(){
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String html = response.body().string();
                    Log.d("b===" ,html);
                    setData(html);

                    String rankUrl = SoDuUrl.bookShelfPage;
                    loadData(rankUrl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                onRequestFailure();
            }
        });

//        HttpHelper.postMethod(url, new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try {
//
//                    String html = response.body().string();
//                    setData(html);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                onRequestFailure();
//            }
//        });
    }

    @Override
    public void onFragmentUnVisible() {

    }

    @Override
    public void setData(String html) {

        TextView txt = (TextView) this.getView().findViewById(R.id.txt_rank_content_online_shelft);
        txt.setText(html);
        isLoaded = true;
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
