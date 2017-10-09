package com.ruobin.sodu.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ruobin.sodu.MyApplication;

/**
 * Created by ruobin on 2017/10/9.
 */
public class LogonService {


    private static final String COOKIE_PREF = "cookies_prefs";
    private static final String COOKIE_NAME = "sodu_user";

    public static boolean isLogon() {

        SharedPreferences sp = MyApplication.getContextObject().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(sp.getString(COOKIE_NAME, ""))) {

            return true;
        }
        return false;
    }

    public static void removeCookie() {

        SharedPreferences sp = MyApplication.getContextObject().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(COOKIE_NAME);
        editor.apply();
    }

}
