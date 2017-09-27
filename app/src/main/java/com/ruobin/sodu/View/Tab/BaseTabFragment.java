package com.ruobin.sodu.View.Tab;

import android.support.v4.app.Fragment;
import android.view.View;

import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Util.HttpHelper;
import com.ruobin.sodu.Interface.IHtmlRequestResult;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ruobin on 2017/9/26.
 */
public abstract class BaseTabFragment extends Fragment {

    public List<Book> books = new ArrayList<Book>();

    protected View currentView;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            //相当于Fragment的onResume
            onFragmentVisible();
        } else {
            //相当于Fragment的onPause
            onFragmentUnVisible();
        }
    }

    public void getHtmlByUrl(String url, final IHtmlRequestResult result) {

        HttpHelper.getMethod(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String html = response.body().string();
                    result.success(html);

                } catch (Exception e) {
                    e.printStackTrace();
                    result.error();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.error();
            }
        });
    }



    //检测是否需要加载数据
    public boolean ifNeedLoadData() {

        if (books.size() == 0) {
            return true;
        }
        return false;
    }


    //获取数据失败时
    public  void onRequestFailure() {};

    //当fragment隐藏时
    public  void onFragmentUnVisible() { };


    //当fragment显示时
    public abstract void onFragmentVisible();

    //获取数据
    public abstract void loadData(String url);

    //设置页面数据方法
    public abstract void setData(String html);

    //点击
    public abstract void itemClick(View view, int position);

    //长按
    public abstract void itemLongClick(View view, int position);

    //初始化列表项数据
    public abstract void itemInitData(View view, Object item);
}
