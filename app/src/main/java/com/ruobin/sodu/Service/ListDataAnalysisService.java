package com.ruobin.sodu.Service;

import com.ruobin.sodu.Model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ruobin on 2017/9/28.
 */
public class ListDataAnalysisService {

    public static List<Book> AnalysisOnlineShelfDatas(String html) {

        List<Book> list = new ArrayList<Book>();

        if (html == null || html.equals("")) {
            return null;
        }
        html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
        Pattern p = Pattern.compile("<div class=\"main-html\".*?\"clearSc\".*?</div>");

        Matcher matcher = p.matcher(html);

        while (matcher.find()) {
            try {
                String temp = matcher.group();
                Book book = new Book();

                Matcher nameMather = Pattern.compile("<a href=\"(.*?)\".*?>(.*?)<\\/a>.*?<a href=\\\".*?\\\".*?>(.*?)<\\/a>.*?;\\\">(.*?)<\\/div>.*?id=(.*?)\\\"").matcher(temp);
                if (nameMather.find()) {

                    book.BookName = nameMather.group(2);
                    book.BookId = nameMather.group(5);
                    book.NewestCatalogName = nameMather.group(3);
                    book.LastReadCatalogName = nameMather.group(3);
                    book.UpdateTime = nameMather.group(4);
                    book.SoDuUpdateCatalogUrl = nameMather.group(1);
                    list.add(book);
                }
            } catch (Exception ex) {

                continue;
            }
        }
        return list;
    }

    public static List<Book> AnalysisRankDatas(String html) {

        List<Book> list = new ArrayList<Book>();

        if (html == null || html.equals("")) {
            return null;
        }
        html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
        Pattern p = Pattern.compile("<div class=\"main-html\".*?<div style=\"width:88px;float:left;\">.*?</div>");

        Matcher matcher = p.matcher(html);

        while (matcher.find()) {
            try {
                String temp = matcher.group();
                Book book = new Book();

                Matcher nameMather = Pattern.compile("<a href=\"(.*?)\".*?>.*?addToFav\\((.*?), \'(.*?)\'.*?<a.*?>(.*?)</a>.*?88.*>(.*?)</div>").matcher(temp);
                if (nameMather.find()) {

                    book.BookName = nameMather.group(3);
                    book.BookId = nameMather.group(2);
                    book.NewestCatalogName = nameMather.group(4);
                    book.LastReadCatalogName = nameMather.group(4);
                    book.UpdateTime = nameMather.group(5);
                    book.SoDuUpdateCatalogUrl = nameMather.group(1);

                    Matcher rangeMatcher = Pattern.compile("<small class=\"(.*?)\">(.*?)</small>").matcher(temp);
                    if (rangeMatcher.find()) {
                        book.RankValue = rangeMatcher.group(1).equals("trend-up") ? rangeMatcher.group(2) : "-" + rangeMatcher.group(2);
                    }

                    list.add(book);
                }
            } catch (Exception ex) {

                continue;
            }
        }
        return list;
    }

    public static List<Book> AnalysisHotRecommendDatas(String html) {

        List<Book> list = new ArrayList<Book>();

        if (html == null || html.equals("")) {
            return null;
        }
        html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
        Pattern p = Pattern.compile("<div class=\"main-head\">.*?<table");
        Matcher matcher = p.matcher(html);
        if (matcher.find()) {
            try {
                String temp = matcher.group();

                String[] strs = temp.split("<div class=\"main-head\">");

                if (strs.length == 0) {
                    return null;
                }

                List<Book> hots = commonAnaylisisBooks(strs[3]);

                if (hots != null) {
                    list.addAll(hots);
                }

                List<Book> recommends = commonAnaylisisBooks(strs[2]);
                if (recommends != null) {
                    list.addAll(recommends);
                }

            } catch (Exception ex) {
                list = null;
            }
        }
        return list;
    }

    public static List<Book> commonAnaylisisBooks(String html) {

        List<Book> list = new ArrayList<Book>();
        try {
            Matcher matcher = Pattern.compile("<div class=\"main-html\".*?class=xt1>.*?</div>").matcher(html);
            while (matcher.find()) {
                String temp = matcher.group();
                Matcher tempM = Pattern.compile("<a href=\"(.*?)\".*?>.*?addToFav\\((.*?), \'(.*?)\'.*?<a.*?>(.*?)</a>.*?88.*>(.*?)</div>").matcher(temp);
                if (tempM.find()) {
                    Book book = new Book();
                    book.BookName = tempM.group(3);
                    book.BookId = tempM.group(2);
                    book.NewestCatalogName = tempM.group(4);
                    book.LastReadCatalogName = tempM.group(4);
                    book.UpdateTime = tempM.group(5);
                    book.SoDuUpdateCatalogUrl = tempM.group(1);
                    list.add(book);
                }
            }
        } catch (Exception ex) {
            list = null;
        }
        return list;
    }

    public static List<Book> getUpdateCatalogList(String html,String bookId,String bookName) {
        List<Book> list = new ArrayList<Book>();
        try {

            html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
            Matcher matcher = Pattern.compile("<div class=\"main-html\".*?class=\"xt1\">.*?</div>").matcher(html);
            while (matcher.find()) {
                String temp = matcher.group();
                Matcher tempM = Pattern.compile("<a href=.*?chapterurl=(.*?)\".*?alt=\"(.*?)\".*?tl\">(.*?)<.*?xt1\">(.*?)</div>").matcher(temp);
                if (tempM.find()) {
                    Book book = new Book();
                    book.BookId = bookId;
                    book.BookName = bookName;
                    book.NewestCatalogName = tempM.group(2);
                    book.LyWeb = tempM.group(3);
                    book.UpdateTime = tempM.group(4);
                    book.NewestCatalogUrl = tempM.group(1);
                    list.add(book);
                }
            }
        } catch (Exception ex) {
            list = null;
        }
        return list;
    }

    public static int getUpdateCatalogPageCount(String html) {

        int count = 0;

        try{
            html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
            Matcher matcher = Pattern.compile("总计.*?共(.*?)页").matcher(html);
            if (matcher.find()) {
                String temp = matcher.group(1);
                temp = temp.trim();
                count = Integer.parseInt(temp);
            }
        }catch (Exception ex){

            return  0 ;
        }


        return  count;
    }

    public static List<Book> AnalysisSearchDatas(String html) {

        List<Book> list = new ArrayList<Book>();

        if (html == null || html.equals("")) {
            return null;
        }
        html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
        Pattern p = Pattern.compile("<div class=\"main-html\".*?<div style=\"width:88px;float:left;\">.*?</div>");

        Matcher matcher = p.matcher(html);

        while (matcher.find()) {
            try {
                String temp = matcher.group();
                Book book = new Book();

                Matcher nameMather = Pattern.compile("<a href=\"(.*?)\".*?>(.*?)<\\/a>.*?<a href=\\\".*?\\\".*?>(.*?)<\\/a>.*?;\\\">(.*?)<\\/div>.*?id=(.*?)\\\"").matcher(temp);
                if (nameMather.find()) {

                    book.BookName = nameMather.group(2);
                    book.BookId = nameMather.group(5);
                    book.NewestCatalogName = nameMather.group(3);
                    book.UpdateTime = nameMather.group(4);
                    book.SoDuUpdateCatalogUrl = nameMather.group(1);
                    list.add(book);
                }
            } catch (Exception ex) {

                continue;
            }
        }
        return list;
    }
}
