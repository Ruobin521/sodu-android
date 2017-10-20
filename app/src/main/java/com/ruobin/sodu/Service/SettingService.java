package com.ruobin.sodu.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.ruobin.sodu.MyApplication;

/**
 * Created by ruobin on 2017/10/11.
 */
public class SettingService {


    public final static String SETTING = "Setting";

    public enum SettingOption {

        //用户名
        UserName,
        //是否自动添加至在在线书架
        IsAutoAddToOnlineShelf,
        //是否在流量下下载
        IsDownloadOnWaan,
        //字体大小
        FontSize,
        //总共八种颜色可选，记录选中的index
        ContentColorIndex,
        //是否为夜间模式
        IsNightMode,
        //行高
        LineHeight,
        //亮度
        LightValue,
    }

    private Context mContext;
    private static SettingService service;
    private SharedPreferences sp;

    private SettingService(Context mContext) {
        this.mContext = mContext.getApplicationContext();
        sp = this.mContext.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }

    public static synchronized SettingService getInstance() {
        return service;
    }

    public static synchronized SettingService createConfig(Context context) {
        if (service == null) {
            service = new SettingService(context);
        }
        return service;
    }


    public int getFontSize() {
        return getValue(SettingOption.FontSize.name(), (int) 20);
    }

    public void setFontSize(int fontSize) {
        putValue(SettingOption.FontSize.name(), fontSize);
    }

    public int getLineSpace() {
        return getValue(SettingOption.LineHeight.name(), (int) 20);
    }

    public void setLineSpace(int fontSize) {
        putValue(SettingOption.LineHeight.name(), fontSize);
    }


    public boolean getIsNightMode() {
        return getValue(SettingOption.IsNightMode.name(), false);
    }

    public void setIsNightMode(boolean isNightMode) {
        putValue(SettingOption.IsNightMode.name(), isNightMode);
    }



    public void putValue(String key, int value) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(key, value);
        ed.commit();
    }

    public void putValue(String key, boolean value) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean(key, value);
        ed.commit();
    }

    public void putValue(String key, String value) {
        SharedPreferences.Editor ed = sp.edit();
        ed.putString(key, value);
        ed.commit();
    }

    public int getValue(String key, int defValue) {
        int value = sp.getInt(key, defValue);
        return value;
    }

    public float getValue(String key, float defValue) {
        float value = sp.getFloat(key, defValue);
        return value;
    }


    public boolean getValue(String key, boolean defValue) {
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public String getValue(String key, String defValue) {
        String value = sp.getString(key, defValue);
        return value;
    }

    public void removeValue(String key) {
        SharedPreferences.Editor ed = sp.edit();
        ed.remove(key);
        ed.commit();
    }

}
