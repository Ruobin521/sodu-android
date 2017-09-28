package com.ruobin.sodu.View.Tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.ListDataAnalysisService;

import java.util.List;


public class Tab_Rank extends BaseTabFragment {

    private int pageIndex = 0;
    private int pageCount = 8;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setId(R.layout.fragment_tab_rank,R.layout.item_rank);
    }


    @Override
    public void loadData() {

        super.loadData();
        String url = SoDuUrl.rank;

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

        if(html == null || html == ""){
            return;
        }

        List<Book> list  = ListDataAnalysisService.AnalysisRankDatas(html);

        books = list;
        super.setData(html);
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

}
