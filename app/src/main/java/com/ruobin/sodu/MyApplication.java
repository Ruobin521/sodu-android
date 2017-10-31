package com.ruobin.sodu;

import android.app.Application;
import android.content.Context;

import com.ruobin.sodu.Service.SettingService;
import com.ruobin.sodu.View.PageReader.PageHelper;

/**
 * Created by ruobin on 2017/9/26.
 */
public class MyApplication extends Application {

    private static Context context;

    //返回
    public static Context getContextObject(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SettingService.createConfig(this);
        PageHelper.createPageFactory(this);
    }

}
