package com.ruobin.sodu;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.Util.HttpHelper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseListViewActivity {

    LinearLayout loading;

    public SearchActivity() {
        layoutId = R.layout.activity_search;
        itemLayoutId = R.layout.item_rank;

    }


    public void initView() {

        loading = (LinearLayout) findViewById(R.id.loading);

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.navigation_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Button btnSearch = (Button) findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });


        EditText text = (EditText) findViewById(R.id.txt_search);
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    loadData();
                }
                return false;
            }
        });

    }


    @Override
    public void loadData() {
        try {
            EditText text = (EditText) findViewById(R.id.txt_search);

            if (TextUtils.isEmpty(text.getText().toString())) {
                Toast.makeText(this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                return;
            }
            String searchPara = text.getText().toString();
            searchPara = URLEncoder.encode(searchPara, "UTF-8");
            //Toast.makeText(this,searchPara,Toast.LENGTH_SHORT).show();
            search(searchPara);

        } catch (Exception ex) {
        }
    }


    private void search(String para) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }

        loading.setVisibility(View.VISIBLE);

        String url = SoDuUrl.bookSearchPage + para;

        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {
                loading.setVisibility(View.GONE);
                updateData(html);
            }

            @Override
            public void error() {
                loading.setVisibility(View.GONE);
                onRequestFailure();
            }
        });

    }

    @Override
    public void updateData(String html) {

        books.clear();

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
        books.addAll(list);
        super.updateData(html);
    }


    public void itemClick(View view, int position) {

        Book book = books.get(position);
        Intent intent = new Intent();
        intent.setClass(this, UpdateCatalogActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);

    }


    public void itemLongClick(View view, int position) {
        Toast.makeText(this, "long click " + position + " item", Toast.LENGTH_SHORT).show();
    }


    public void itemInitData(View view, Object item) {

        TextView tv = (TextView) view.findViewById(R.id.item_rank_name);
        tv.setText(((Book) item).BookName);

        TextView tv2 = (TextView) view.findViewById(R.id.item_rank_catalog_name);
        tv2.setText(((Book) item).NewestCatalogName);

        TextView tv3 = (TextView) view.findViewById(R.id.item_rank_update_time);
        tv3.setText(((Book) item).UpdateTime);
    }


    @Override
    public void onRequestFailure() {

        Toast.makeText(this, "搜索失败", Toast.LENGTH_SHORT).show();

    }
}
