package com.ruobin.sodu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Service.LogonService;
import com.ruobin.sodu.View.Tab.Tab_Hot;
import com.ruobin.sodu.View.Tab.Tab_LocalShelf;
import com.ruobin.sodu.View.Tab.Tab_OnlineShelf;
import com.ruobin.sodu.View.Tab.Tab_Rank;
import com.ruobin.sodu.View.Tab.Tab_Setting;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {

    public static MainActivity Instance;

    private ViewPager mViewPager;
    private FragmentStatePagerAdapter mAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();

//    private LinearLayout mTabBtnOnlineShelf;
//    private LinearLayout mTabBtnRank;
//    private LinearLayout mTabBtnHot;
//    private LinearLayout mTabBtnLocalShelf;
//    private LinearLayout mTabBtnSetting;

    private List<TabEntity> tabs = new ArrayList<TabEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Instance = this;
        initView();
        initViewPager();

    }

    protected void resetTabBtn() {

        for (TabEntity tab : tabs) {
            tab.setTabBtnNormal();
        }
    }

    private void initView() {

        mFragments.clear();
        tabs.clear();

        if (LogonService.isLogon()) {
            findViewById(R.id.id_tab_bottom_onlineShelf).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.id_tab_bottom_onlineShelf).setVisibility(View.GONE);

        }

        if (LogonService.isLogon()) {
            mFragments.add(new Tab_OnlineShelf());

        }
        mFragments.add(new Tab_Rank());
        mFragments.add(new Tab_Hot());
        mFragments.add(new Tab_LocalShelf());
        mFragments.add(new Tab_Setting());


        if (LogonService.isLogon()) {
            tabs.add(new TabEntity(
                    R.id.id_tab_bottom_onlineShelf,
                    R.id.btn_tab_bottom_onlineshelf,
                    R.id.txt_tab_bottom_onlineshelf,
                    R.drawable.tabbar_online_shelf,
                    R.drawable.tabbar_online_shelf_selected)
            );
        }

        tabs.add(new TabEntity(
                R.id.id_tab_bottom_rank,
                R.id.btn_tab_bottom_rank,
                R.id.txt_tab_bottom_rank,
                R.drawable.tabbar_rank,
                R.drawable.tabbar_rank_selected)
        );

        tabs.add(new TabEntity(
                R.id.id_tab_bottom_hotrecommend,
                R.id.btn_tab_bottom_hotrecommend,
                R.id.txt_tab_bottom_hotrecommend,
                R.drawable.tabbar_hot,
                R.drawable.tabbar_hot_selected)
        );

        tabs.add(new TabEntity(
                R.id.id_tab_bottom_localshelf,
                R.id.btn_tab_bottom_localshelf,
                R.id.txt_tab_bottom_localshelf,
                R.drawable.tabbar_local_shelf,
                R.drawable.tabbar_local_shelf_selected)
        );

        tabs.add(new TabEntity(
                R.id.id_tab_bottom_setting,
                R.id.btn_tab_bottom_setting,
                R.id.txt_tab_bottom_setting,
                R.drawable.tabbar_setting,
                R.drawable.tabbar_setting_selected)
        );


        tabs.get(0).setTabBtnHighlight();
    }


    private void initViewPager() {

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        // mViewPager.setOffscreenPageLimit(5);

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

            @Override
            public void onPageSelected(int position) {

                resetTabBtn();

                TabEntity tab = tabs.get(position);

                tab.setTabBtnHighlight();

                if(position == tabs.size() -1 && !LogonService.isLogon()) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.Instance, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {


            }
        });

    }


    public void setLogon(boolean isLogon) {

        try{
            if (isLogon) {
                mFragments.add(0,new Tab_OnlineShelf());
                tabs.add(0, new TabEntity(
                        R.id.id_tab_bottom_onlineShelf,
                        R.id.btn_tab_bottom_onlineshelf,
                        R.id.txt_tab_bottom_onlineshelf,
                        R.drawable.tabbar_online_shelf,
                        R.drawable.tabbar_online_shelf_selected)
                );
                findViewById(R.id.id_tab_bottom_onlineShelf).setVisibility(View.VISIBLE);
            }else{

                mFragments.remove(0);
                tabs.remove(0);
                findViewById(R.id.id_tab_bottom_onlineShelf).setVisibility(View.GONE);
            }
            initViewPager();

        }catch (Exception ex){
            Log.d("error:",ex.getMessage().toString());
        }
    }

    @Override
    public void onBackPressed() {
        showQuitTips();
    }

    // 记录第一次按下的时间
    private long firstPressTime = -1;
    // 记录第二次按下的时间
    private long lastPressTime;
    // 两次按下的时间间隔
    private final long INTERVAL = 2000;

    private void showQuitTips() {

        lastPressTime = System.currentTimeMillis();
        if (lastPressTime - firstPressTime <= INTERVAL) {
            System.exit(0);
        } else {
            firstPressTime = lastPressTime;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
        }
    }


    public class TabEntity {

        private LinearLayout layout;

        public int btnLayoutId;
        public int imgBtnId;
        public int txtId;
        public int imgBtnCommonSrcId;
        public int imgBtnHightLightSrcId;

        public TabEntity(int layoutId, int btnId, int textId, int commonSrcId, int hightLightScrId) {

            btnLayoutId = layoutId;
            imgBtnId = btnId;
            txtId = textId;
            imgBtnCommonSrcId = commonSrcId;
            imgBtnHightLightSrcId = hightLightScrId;

            layout = (LinearLayout) findViewById(layoutId);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = -1;
                    for (int i = 0; i < tabs.size(); i++) {

                        if (tabs.get(i).layout == view) {
                            index = i;
                            break;
                        }
                    }
                    mViewPager.setCurrentItem(index, false);
                }
            });
        }


        public void setTabBtnNormal() {
            ((ImageButton) layout.findViewById(imgBtnId)).setBackground(getResources().getDrawable(imgBtnCommonSrcId));
            ((TextView) layout.findViewById(txtId)).setTextColor(getResources().getColor(R.color.tabText));
        }


        public void setTabBtnHighlight() {

            ((ImageButton) layout.findViewById(imgBtnId)).setBackground(getResources().getDrawable(imgBtnHightLightSrcId));
            ((TextView) layout.findViewById(txtId)).setTextColor(getResources().getColor(R.color.tabTextHighLight));
        }

    }

}




