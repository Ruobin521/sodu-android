package com.ruobin.sodu.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ruobin on 2017/10/2.
 */
public class SaveCookiesInterceptor implements Interceptor {
    private static final String COOKIE_PREF = "cookies_prefs";
    private Context mContext;

    public SaveCookiesInterceptor(Context context) {
        mContext = context;
    }

    //sodu_user=iUtPHS0jQNCLWt3WQ0ozqw==; domain=.sodu.cc; expires=Thu, 12-Oct-2017 02:14:56 GMT; path=/
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //set-cookie可能为多个
        if (!response.headers("set-cookie").isEmpty()) {
            List<String> cookies = response.headers("set-cookie");

            for (String str: cookies){
                if(!str.contains("sodu_user")) continue;
                String cookieStr = str.replaceAll("20\\d\\d","2030");
                Cookie cookie = Cookie.parse(request.url(), cookieStr);
                saveCookie(cookie.name(),cookieStr);
            }

        }
        return response;
    }





    //整合cookie为唯一字符串
    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set=new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if(set.contains(s))continue;
                set.add(s);
            }
        }

        Iterator<String> ite = set.iterator();
        while (ite.hasNext()) {
            String cookie = ite.next();
            sb.append(cookie).append(";");
        }

        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }

        return sb.toString();
    }


    private void saveCookie(String name, String cookies) {
        SharedPreferences sp = mContext.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

       if (TextUtils.isEmpty(name)) {
            throw new NullPointerException("name is null.");
        }else{
            editor.putString(name, cookies);
        }
        editor.apply();

    }


}