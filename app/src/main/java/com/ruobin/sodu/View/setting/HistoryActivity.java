package com.ruobin.sodu.View.setting;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.ruobin.sodu.BaseListViewActivity;
import com.ruobin.sodu.R;

public class HistoryActivity extends BaseListViewActivity {


    public HistoryActivity() {

        layoutId = R.layout.activity_history;

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


        Button btnClear = (Button) findViewById(R.id.btn_clear_history);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // onBackPressed();
                Toast.makeText(view.getContext(), "清空记录", Toast.LENGTH_SHORT).show();

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
