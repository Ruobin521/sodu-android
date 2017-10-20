package com.ruobin.sodu.View.PageReader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Model.BookCatalog;
import com.ruobin.sodu.Model.MenuMessageEvent;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Util.MyUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PageReaderActivity extends AppCompatActivity {

    ScanView scanview;
    ScanViewAdapter adapter;

    View topBar;
    View bottomBar;
    View addBtn;

    boolean isShwoMenu = false;

    private Book book;

    private int downX = 0;
    private int downY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_reader);
        setStatusBarVisiablity(false);

        Intent intent = getIntent();
        book = (Book) intent.getSerializableExtra("book");

        ///   EventBus.getDefault().register(this);
        initView();

    }


    @Override
    protected void onStart() {
        super.onStart();
        setStatusBarVisiablity(isShwoMenu);
    }

    private void initView() {

        topBar = findViewById(R.id.reader_top_bar);
        bottomBar = findViewById(R.id.reader_bottom_bar);
        addBtn = findViewById(R.id.reader_btn_add);

        topBar.setVisibility(View.GONE);
        bottomBar.setVisibility(View.GONE);
        addBtn.setVisibility(View.GONE);

        scanview = (ScanView) findViewById(R.id.scanview);

        TextView txtBookName = (TextView) findViewById(R.id.reader_txt_book_name);
        txtBookName.setText(book.BookName);

        adapter = new ScanViewAdapter(this, book);

        scanview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    downX = (int) event.getX();
                    downY = (int) event.getY();
                }

                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (scanview.state == scanview.STATE_MOVE) {
                    return false;
                }
                float screenWidth = MyUtils.getScreenWidth(v.getContext());
                float screenHeight = MyUtils.getScreenHeight(v.getContext());

                if (isShwoMenu) {
                    setMenuVisiablity(false);
                } else {
                    if (downX > screenWidth / 3 && downX < screenWidth * 2 / 3 && downY > screenHeight / 3 && downY < screenHeight * 2 / 3) {
                        setMenuVisiablity(true);
                    } else {

                    }
                }
                return false;
            }
        });


        adapter.loadCatalogData(adapter.currentCatalog, new IHtmlRequestResult() {
            @Override
            public void success(String html) {

                adapter.items = adapter.splitHtmlToPages(adapter.currentCatalog.CatalogUrl, html);
                scanview.setAdapter(adapter);
            }

            @Override
            public void error() {

            }

            @Override
            public void end() {

            }
        });

    }


    private void setMenuVisiablity(boolean isShow) {
        setBottomBarVisiablity(isShow);
        setTopBarVisiablity(isShow);
        setStatusBarVisiablity(isShow);
        isShwoMenu = isShow;
        scanview.isShowMenu = isShow;
        setAddBtnVisiability(isShow);
    }

    private void setBottomBarVisiablity(final boolean isVisiable) {
        final RelativeLayout bottomBar = (RelativeLayout) findViewById(R.id.reader_bottom_bar);
        Animation animation = AnimationUtils.loadAnimation(this, isVisiable ? R.anim.reader_bottom_bar_slide_in : R.anim.reader_bottom_bar_slide_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bottomBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomBar.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bottomBar.startAnimation(animation);
    }

    private void setTopBarVisiablity(final boolean isVisiable) {
        final View topBar = findViewById(R.id.reader_top_bar);
        Animation animation = AnimationUtils.loadAnimation(this, isVisiable ? R.anim.reader_top_bar_slide_in : R.anim.reader_top_bar_slide_out);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                topBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                topBar.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        topBar.startAnimation(animation);
    }

    private void setStatusBarVisiablity(boolean isVisable) {
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(isVisable ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN :View.SYSTEM_UI_FLAG_FULLSCREEN );

        if (isVisable) {
            showSystemUI();
        } else {
            hideSystemUI();
        }
    }


    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        //  | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void setAddBtnVisiability(boolean isVisable) {

        addBtn.setVisibility(isVisable ? View.VISIBLE : View.GONE);

    }

}
