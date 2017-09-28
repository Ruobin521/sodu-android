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

    /**
     * 转义正则特殊字符
     *
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {
        String[] fbsArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"};
        for (String key : fbsArr) {
            if (keyword.contains(key)) {
                keyword = keyword.replace(key, "\\" + key);
            }
        }
        return keyword;
    }

    public static List<Book> AnalysisRankDatas(String html) {

        List<Book> list = new ArrayList<Book>();

        if (html == null || html.equals("")) {
            return null;
        }

        html = html.replaceAll("\r", "").replaceAll("\t", "").replaceAll("\n", "");
        //  html = escapeExprSpecialWord(html);
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
                    book.UpdateTime = nameMather.group(5);
                    book.SoDuUpdateCatalogUrl = nameMather.group(1);

                    Matcher rangeMatcher = Pattern.compile("<small class=\"(.*?)\">(.*?)</small>").matcher(temp);
                    if(rangeMatcher.find()){
                        book.RankValue = rangeMatcher.group(1).equals("trend-up") ? rangeMatcher.group(2) : "-"  + rangeMatcher.group(2);
                    }

                    list.add(book);
                }
            } catch (Exception ex) {

                continue;
            }
        }
        return list;
    }
}
