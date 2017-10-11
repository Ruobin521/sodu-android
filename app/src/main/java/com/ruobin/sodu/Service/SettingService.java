package com.ruobin.sodu.Service;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ruobin on 2017/10/11.
 */
public class SettingService {

    public final static String SETTING = "Setting";

    public static enum SettingOption {

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
        //横屏
        IsLandscape,
        //行高
        LineHeight,
        //亮度
        LightValue,
        //阅读模式（分页动画，滚动）
        IsScroll,

    }

    public static void putValue(Context context, String key, int value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.putInt(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, boolean value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.putBoolean(key, value);
        sp.commit();
    }

    public static void putValue(Context context, String key, String value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.putString(key, value);
        sp.commit();
    }

    public static int getValue(Context context, String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }

    public static boolean getValue(Context context, String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }

    public static String getValue(Context context, String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }

    public static void removeValue(Context context, String key) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE).edit();
        sp.remove(key);
    }
}
