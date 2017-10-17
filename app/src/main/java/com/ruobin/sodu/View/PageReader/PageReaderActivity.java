package com.ruobin.sodu.View.PageReader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ruobin.sodu.R;

import java.util.ArrayList;
import java.util.List;

public class PageReaderActivity extends AppCompatActivity {

    ScanView scanview;
    ScanViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_reader);

        View decorView = getWindow().getDecorView();
      // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
//
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initView();
    }

    private void initView() {

        scanview = (ScanView) findViewById(R.id.scanview);
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < 8; i++)
            items.add("页面索引" + (i + 1) + "页");
        adapter = new ScanViewAdapter(this, items);
        scanview.setAdapter(adapter);


        View view = (ScanView) findViewById(R.id.scanview);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBottomBarVisiablity();
                setTopBarVisiablity();
                setStatusBarVisiablity();
            }
        });
    }


    private void setBottomBarVisiablity() {

        final RelativeLayout bottomBar = (RelativeLayout) findViewById(R.id.reader_bottom_bar);

        final boolean isVisiable = (bottomBar.getVisibility() == View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(this, isVisiable ? R.anim.reader_bottom_bar_slide_out : R.anim.reader_bottom_bar_slide_in);
//        animation.setFillEnabled(true);
//        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                  bottomBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                 bottomBar.setVisibility(isVisiable ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bottomBar.startAnimation(animation);
    }

    private void setTopBarVisiablity() {

        final View topBar = findViewById(R.id.reader_top_bar);

        final boolean isVisiable = (topBar.getVisibility() == View.VISIBLE);
        topBar.setVisibility(View.VISIBLE);

        Animation animation = AnimationUtils.loadAnimation(this, isVisiable ? R.anim.reader_top_bar_slide_out : R.anim.reader_top_bar_slide_in);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                  topBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                topBar.setVisibility(isVisiable ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        topBar.startAnimation(animation);
    }

    private void setStatusBarVisiablity() {

//        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        int status = decorView.getSystemUiVisibility();
        decorView.setSystemUiVisibility(status == View.SYSTEM_UI_FLAG_FULLSCREEN ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN :View.SYSTEM_UI_FLAG_FULLSCREEN );
//
//      //
    }
}
