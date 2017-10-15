package com.ruobin.sodu.Constants;

/**
 * Created by ruobin on 2017/9/26.
 */
public class SoDuUrl {

    //首页
    public static String home = "http://www.sodu.cc";

    //排行榜
    public static String rank = "http://www.sodu.cc/top.html";

    public static String getRankByIndex(int index) {

        return "http://www.sodu.cc/top_"+index+".html";
    }

    //登录页面
    public static String loginPostPage = "http://www.sodu.cc/handler/login.html";

    //个人书架    http://www.sodu.cc/home.html?id=339807
    public static String bookShelfPage = "http://www.sodu.cc/home.html";

    //取消收藏
    public static String removeOnlineBookPage = "http://www.sodu.cc/home.html?id=";

    //搜索
    public static String bookSearchPage = "http://www.sodu.cc/result.html?searchstr=";

    ///添加至书架
    public static String addToShelfPage = "http://www.sodu.cc/handler/home.html?bid=";
}
