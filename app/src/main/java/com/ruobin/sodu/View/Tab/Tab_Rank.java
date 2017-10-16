package com.ruobin.sodu.View.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.DBHelper.BookCacheDao;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.UpdateCatalogActivity;
import com.ruobin.sodu.Util.HttpHelper;
import com.ruobin.sodu.View.MenuPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class Tab_Rank extends BaseTabFragment {

    private int pageIndex = 0;
    private int pageCount = 8;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setId(R.layout.fragment_tab_rank, R.layout.item_rank, true, BookCacheDao.BookCacheType.Rank);
    }



    @Override
    public void loadData() {
        super.loadData();
        loadDataByIndex(1);
    }

    @Override
    public void loadMoreData() {
        if (pageIndex >= pageCount) {
            endLoad();
            return;
        } else {
            loadDataByIndex(pageIndex + 1);
        }
    }

    private void loadDataByIndex(int index) {

        this.currentView.findViewById(R.id.refresh_error).setVisibility(View.GONE);

        String url = "";
        if (index == 1) {
            url = SoDuUrl.rank;
        } else {
            url = SoDuUrl.getRankByIndex(index);
        }

        pageIndex = index;
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

        if (html == null || html == "") {
            return;
        }
        List<Book> list = ListDataAnalysisService.AnalysisRankDatas(html);
        if (list == null) {
            return;
        }
        if (books == null) {
            books = new ArrayList<Book>();
        }

        if (pageIndex == 1) {
            books.clear();
        }
        books.addAll(list);
        updateUI();
    }

    @Override
    public void updateUI() {

        TextView tx = (TextView) currentView.findViewById(R.id.tab_rank_title);
        StringBuilder stringBuilder = new StringBuilder("排行榜")
                .append("-")
                .append(pageIndex)
                .append("/")
                .append(pageCount);

        tx.setText(stringBuilder.toString());
        super.updateUI();
        super.endLoad();
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
        Book book = books.get(position);
        MenuPopupWindow popupView = new MenuPopupWindow(this.getActivity(),R.layout.popup_add_online,book);
        popupView.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void itemInitData(View view, Object item) {

        TextView tv = (TextView) view.findViewById(R.id.item_rank_name);
        tv.setText(((Book) item).BookName);

        TextView tv2 = (TextView) view.findViewById(R.id.item_rank_catalog_name);
        tv2.setText(((Book) item).NewestCatalogName);

        TextView tv3 = (TextView) view.findViewById(R.id.item_rank_update_time);
        tv3.setText(((Book) item).UpdateTime);

        LinearLayout range = (LinearLayout) view.findViewById(R.id.rank_value_arrow);
        range.setVisibility(View.VISIBLE);

        TextView tv4 = (TextView) view.findViewById(R.id.item_rank_value);
        ImageView img = (ImageView) view.findViewById(R.id.rank_item_arrow_img);

        String rangeValue = ((Book) item).RankValue;

        if (rangeValue == null || "".equals(rangeValue)) {
            tv4.setText("-");
        } else if (rangeValue.startsWith("-")) {
            img.setImageDrawable(getResources().getDrawable(R.drawable.down));
            tv4.setText(rangeValue.replace("-", ""));
        } else {
            img.setImageDrawable(getResources().getDrawable(R.drawable.up));
            tv4.setText(rangeValue);
        }
    }


}
