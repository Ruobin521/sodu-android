package com.ruobin.sodu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruobin.sodu.View.Tab.Tab_Hot;
import com.ruobin.sodu.View.Tab.Tab_LocalShelf;
import com.ruobin.sodu.View.Tab.Tab_OnlineShelf;
import com.ruobin.sodu.View.Tab.Tab_Rank;
import com.ruobin.sodu.View.Tab.Tab_Setting;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private FragmentStatePagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();


    private LinearLayout mTabBtnOnlineShelf;
    private LinearLayout mTabBtnRank;
    private LinearLayout mTabBtnHot;
    private LinearLayout mTabBtnLocalShelf;
    private LinearLayout mTabBtnSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        // mViewPager.setOffscreenPageLimit(5);

        initView();

        mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {

                return mFragments.get(arg0);

            }
        };

        mViewPager.setAdapter(mAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentIndex;

            @Override
            public void onPageSelected(int position) {

                resetTabBtn();

                switch (position) {
                    case 0:
                        setTabBtnHighlight(mTabBtnOnlineShelf, R.id.btn_tab_bottom_onlineshelf, R.id.txt_tab_bottom_onlineshelf, R.drawable.tabbar_online_shelf_selected);
                        break;
                    case 1:
                        setTabBtnHighlight(mTabBtnRank, R.id.btn_tab_bottom_rank, R.id.txt_tab_bottom_rank, R.drawable.tabbar_rank_selected);
                        break;
                    case 2:
                        setTabBtnHighlight(mTabBtnHot, R.id.btn_tab_bottom_hotrecommend, R.id.txt_tab_bottom_hotrecommend, R.drawable.tabbar_hot_selected);
                        break;
                    case 3:
                        setTabBtnHighlight(mTabBtnLocalShelf, R.id.btn_tab_bottom_localshelf, R.id.txt_tab_bottom_localshelf, R.drawable.tabbar_local_shelf_selected);
                        break;
                    case 4:
                        setTabBtnHighlight(mTabBtnSetting, R.id.btn_tab_bottom_setting, R.id.txt_tab_bottom_setting, R.drawable.tabbar_setting_selected);
                        break;
                }

                currentIndex = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {


            }
        });
    }

    protected void resetTabBtn() {

        setTabBtnNormal(mTabBtnOnlineShelf, R.id.btn_tab_bottom_onlineshelf, R.id.txt_tab_bottom_onlineshelf, R.drawable.tabbar_online_shelf);
        setTabBtnNormal(mTabBtnRank, R.id.btn_tab_bottom_rank, R.id.txt_tab_bottom_rank, R.drawable.tabbar_rank);
        setTabBtnNormal(mTabBtnHot, R.id.btn_tab_bottom_hotrecommend, R.id.txt_tab_bottom_hotrecommend, R.drawable.tabbar_hot);
        setTabBtnNormal(mTabBtnLocalShelf, R.id.btn_tab_bottom_localshelf, R.id.txt_tab_bottom_localshelf, R.drawable.tabbar_local_shelf);
        setTabBtnNormal(mTabBtnSetting, R.id.btn_tab_bottom_setting, R.id.txt_tab_bottom_setting, R.drawable.tabbar_setting);
    }


    private void initView() {

        mTabBtnOnlineShelf = (LinearLayout) findViewById(R.id.id_tab_bottom_onlineShelf);
        mTabBtnRank = (LinearLayout) findViewById(R.id.id_tab_bottom_rank);
        mTabBtnHot = (LinearLayout) findViewById(R.id.id_tab_bottom_hotrecommend);
        mTabBtnLocalShelf = (LinearLayout) findViewById(R.id.id_tab_bottom_localshelf);
        mTabBtnSetting = (LinearLayout) findViewById(R.id.id_tab_bottom_setting);

        final Tab_OnlineShelf online_shelf = new Tab_OnlineShelf();
        final Tab_Rank rank = new Tab_Rank();
        final Tab_Hot hot = new Tab_Hot();
        final Tab_LocalShelf local_shelf = new Tab_LocalShelf();
        final Tab_Setting setting = new Tab_Setting();


        mFragments.add(online_shelf);
        mFragments.add(rank);
        mFragments.add(hot);
        mFragments.add(local_shelf);
        mFragments.add(setting);

        setTabBtnHighlight(mTabBtnOnlineShelf, R.id.btn_tab_bottom_onlineshelf, R.id.txt_tab_bottom_onlineshelf, R.drawable.tabbar_online_shelf_selected);


        mTabBtnOnlineShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0, false);
            }
        });


        mTabBtnOnlineShelf.setOnTouchListener(new onDoubleClick() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = super.onTouch(v, event);

                Log.d("===", String.valueOf(result));
                if (result) {
                    online_shelf.scrollToTop();
                }
                return false;
            }
        });

        mTabBtnRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1, false);
            }
        });

        mTabBtnRank.setOnTouchListener(new onDoubleClick() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = super.onTouch(v, event);

                Log.d("===", String.valueOf(result));
                if (result) {
                    rank.scrollToTop();
                }
                return false;
            }
        });

        mTabBtnHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(2, false);
            }
        });

        mTabBtnHot.setOnTouchListener(new onDoubleClick() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = super.onTouch(v, event);

                Log.d("===", String.valueOf(result));
                if (result) {
                    hot.scrollToTop();
                }
                return false;
            }
        });

        mTabBtnLocalShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(3, false);
            }
        });

        mTabBtnLocalShelf.setOnTouchListener(new onDoubleClick() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean result = super.onTouch(v, event);
                if (result) {
                    local_shelf.scrollToTop();
                }
                return false;
            }
        });


        mTabBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(4, false);
            }
        });

    }


    private void setTabBtnHighlight(LinearLayout parent, int imgBtnId, int txtId, int hightLightImgId) {

        ((ImageButton) parent.findViewById(imgBtnId)).setBackground(getResources().getDrawable(hightLightImgId));
        ((TextView) parent.findViewById(txtId)).setTextColor(getResources().getColor(R.color.tabTextHighLight));
    }

    private void setTabBtnNormal(LinearLayout parent, int imgBtnId, int txtId, int normalImgId) {

        ((ImageButton) parent.findViewById(imgBtnId)).setBackground(getResources().getDrawable(normalImgId));
        ((TextView) parent.findViewById(txtId)).setTextColor(getResources().getColor(R.color.tabText));
    }


    class onDoubleClick implements View.OnTouchListener {

        int count;
        long firClick;
        long secClick;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (MotionEvent.ACTION_DOWN == event.getAction()) {
                count++;

                if (count == 1) {
                    firClick = System.currentTimeMillis();
                } else if (count == 2) {
                    secClick = System.currentTimeMillis();
                    if (secClick - firClick < 1000) {
                        count = 0;
                        firClick = 0;
                        secClick = 0;
                        return true;
                    }else{
                        count = 0;
                        secClick = 0;
                        firClick = secClick;
                    }
                }
            }
            return false;
        }
    }
}




