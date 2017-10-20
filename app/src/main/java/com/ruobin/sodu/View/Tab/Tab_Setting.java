package com.ruobin.sodu.View.Tab;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.LogonService;
import com.ruobin.sodu.Service.SettingService;
import com.ruobin.sodu.View.setting.DownloadCenterActivity;
import com.ruobin.sodu.View.setting.HistoryActivity;
import com.ruobin.sodu.View.setting.LoginActivity;
import com.ruobin.sodu.View.setting.PersonCenterActivity;
import com.suke.widget.SwitchButton;


public class Tab_Setting extends Fragment {


    private View currentView;

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aa===", "Tab_Setting销毁");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentView = inflater.inflate(R.layout.fragment_tab_setting, container, false);
        initView();
        initSwitchSetting();
        return currentView;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            if (currentView == null) {
                return;
            }
            initSwitchSetting();
        }
    }


    private void initView() {
        //初始化个人中心
        RelativeLayout personCenter = (RelativeLayout) currentView.findViewById(R.id.setting_person_center);
        personCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!LogonService.isLogon()) {
                    Intent intent = new Intent();
                    intent.setClass(currentView.getContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    navigateTo(PersonCenterActivity.class);
                }
            }
        });

        //初始化下载中心
        RelativeLayout download = (RelativeLayout) currentView.findViewById(R.id.setting_downliad_center);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateTo(DownloadCenterActivity.class);
            }
        });


        //初始化历史纪录
        RelativeLayout history = (RelativeLayout) currentView.findViewById(R.id.setting_history);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    navigateTo(HistoryActivity.class);
            }
        });


        //初始化IsAutoAddToOnlineShelf
        SwitchButton btn = (SwitchButton) currentView.findViewById(R.id.switch_auto_add_online_shelf);
        btn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                view.setChecked(isChecked);
                SettingService.getInstance().putValue(SettingService.SettingOption.IsAutoAddToOnlineShelf.toString(), isChecked);
            }
        });

        //初始化IsDownloadOnWaan
        SwitchButton btnDownLoadOnWwan = (SwitchButton) currentView.findViewById(R.id.switch_download_on_wwan);
        btnDownLoadOnWwan.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                view.setChecked(isChecked);
                SettingService.getInstance().putValue( SettingService.SettingOption.IsDownloadOnWaan.toString(), isChecked);
            }
        });

        //初始化软件声明
        RelativeLayout btnRjsm = (RelativeLayout) currentView.findViewById(R.id.setting_rjsm);
        btnRjsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(currentView.getContext())
                        .setTitle("软件声明")
                        .setMessage(
                                "　　小说搜索阅读是一款提供网络小说及时更新的工具，为广大小说爱好者提供一种方便，快捷，舒适的阅读体验。\n" +
                                "　　小说搜索阅读中的所有数据均来自第三方，对所有返回的数据概不负责，亦不承担任何责任。\n" +
                                "　　小说搜索阅读鼓励用户支持正版阅读，为网络小说的发展做一份贡献。\n" +
                                "　　在用户使用过程中,基于用户的操作,有可能使用到本地TXT文档。小说搜索阅读只会将数据存储到本地,不会上传任何数据。")
                        .setPositiveButton("确定", null)
                        .show();
            }
        });

        //初始化版本号
        TextView txtVersion = (TextView)currentView.findViewById(R.id.txt_version_name);
        txtVersion.setText("当前版本:" + getVersionName(currentView.getContext()));
    }

    private void initSwitchSetting() {

        SwitchButton btnOnlineShelf = (SwitchButton) currentView.findViewById(R.id.switch_auto_add_online_shelf);
        btnOnlineShelf.setChecked(SettingService.getInstance().getValue( SettingService.SettingOption.IsAutoAddToOnlineShelf.toString(), false));

        SwitchButton btnDownLoadOnWwan = (SwitchButton) currentView.findViewById(R.id.switch_download_on_wwan);
        btnDownLoadOnWwan.setChecked(SettingService.getInstance().getValue(SettingService.SettingOption.IsDownloadOnWaan.toString(), false));
    }

    private void navigateTo(Class t) {
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), t);
        startActivity(intent);
    }


    public String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    public String getVersionName(Context context){
        String versionName="";
        try {
            PackageManager pm = context.getPackageManager();//context为当前Activity上下文
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

}
