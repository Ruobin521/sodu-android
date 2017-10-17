package com.ruobin.sodu.View.PageReader;

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
import android.widget.Toast;

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

    boolean isShwoMenu = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_reader);
        setStatusBarVisiablity(false);
    ///   EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {

        scanview = (ScanView) findViewById(R.id.scanview);
        List<String> items = new ArrayList<String>();
        for (int i = 0; i < 8; i++)
            items.add("页面索引" + (i + 1) + "页");
        adapter = new ScanViewAdapter(this, items);
        scanview.setAdapter(adapter);

//        View view = (ScanView) findViewById(R.id.scanview);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setMenuVisiablity();
//            }
//        });
        scanview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() != MotionEvent.ACTION_UP){
                    return  false;
                }
                if(scanview.state == scanview.STATE_MOVE) {
                    return false;
                }
                float screenWidth = MyUtils.getScreenWidth(v.getContext());
                float screenHeight = MyUtils.getScreenHeight(v.getContext());
                float x = event.getX();
                float y = event.getY();
                if(isShwoMenu){
                    setMenuVisiablity(false);
                }else{
                    if(x>screenWidth/3 &&x<screenWidth*2/3 && y>screenHeight /3 &&y<screenHeight*2/3){
                        setMenuVisiablity(true);
                    }else{

                    }
                }
                return  false;
            }
        });


    }


    private  void setMenuVisiablity(boolean isShow){
        setBottomBarVisiablity(isShow);
        setTopBarVisiablity(isShow);
        setStatusBarVisiablity(isShow);
        isShwoMenu =isShow;
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
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(isVisable ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN :View.SYSTEM_UI_FLAG_FULLSCREEN );
    }
}
