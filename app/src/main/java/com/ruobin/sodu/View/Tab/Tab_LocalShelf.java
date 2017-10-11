package com.ruobin.sodu.View.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.UpdateCatalogActivity;
import com.ruobin.sodu.Util.HttpHelper;

import java.util.List;


public class Tab_LocalShelf extends BaseTabFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setId(R.layout.fragment_tab_local_shelf,R.layout.item_local,false);
    }


    public void loadData() {
        super.loadData();
        String url = SoDuUrl.home;

        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
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

        if(html == null || html == ""){
            return;
        }

        List<Book> list  = ListDataAnalysisService.AnalysisHotRecommendDatas(html);
        books = list;
        super.updateUI();
    }


    @Override
    public void itemClick(View view, int position) {
        Book book = books.get(position);
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), UpdateCatalogActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    @Override
    public void itemLongClick(View view, int position) {

    }

    @Override
    public void itemInitData(View view, Object item) {

        TextView tv = (TextView) view.findViewById(R.id.item_local_book_name);
        tv.setText(((Book) item).BookName);

        TextView tv2 = (TextView) view.findViewById(R.id.item_local_new_chapter_name);
        tv2.setText(((Book) item).NewestCatalogName);

        TextView tv3 = (TextView) view.findViewById(R.id.item_local_old_chapter_name);
        tv3.setText(((Book) item).LastReadCatalogName);
    }



}
