package com.ruobin.sodu.View.PageReader;


import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Service.HtmlAnalysisService;
import com.ruobin.sodu.Service.SettingService;
import com.ruobin.sodu.Util.MyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ds on 2017/10/23.
 */

public class PageHelper {

    private Context mContext;

    private static PageHelper pageHelper;


    private SettingService setting;

    private Paint mPaint;

    //页面宽
    private int mWidth;
    //页面高
    private int mHeight;

    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;

    private float paddingLeft = 10;
    private float paddingTop = 10;
    private float paddingBottom = 10;
    private float paddingRight = 10;

    private float headerBar = 30;
    private float bottomBar = 20;


    // 每页可以显示的行数
    private int mLineCount;

    // 每行可以显示的字数
    private int mColumCount;

    //行间距
    private float m_lineSpace;

    //文字字体大小
    private float m_fontSize;


    public static synchronized PageHelper getInstance() {
        return pageHelper;
    }

    public static synchronized PageHelper createPageFactory(Context context) {
        if (pageHelper == null) {
            pageHelper = new PageHelper(context);
        }
        return pageHelper;
    }


    private PageHelper(Context context) {

        this.mContext = context;

        setting = SettingService.getInstance();

        m_fontSize = MyUtils.dip2px(context, setting.getFontSize());
        m_lineSpace = MyUtils.dip2px(context, setting.getLineSpace());

        paddingLeft = (int) Math.floor(MyUtils.dip2px(context, paddingLeft));
        paddingTop = (int) Math.floor(MyUtils.dip2px(context, paddingTop));
        paddingBottom = (int) Math.floor(MyUtils.dip2px(context, paddingBottom));
        paddingRight = (int) Math.floor(MyUtils.dip2px(context, paddingRight));

        headerBar = (int) Math.ceil(MyUtils.dip2px(context, headerBar));
        bottomBar = (int) Math.ceil(MyUtils.dip2px(context, bottomBar));
//
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
//        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
//        mPaint.setTextSize(m_fontSize);// 字体大小




        mWidth = MyUtils.getScreenWidth(context);
        mHeight = MyUtils.getScreenHeight(context);

        mVisibleWidth = mWidth - paddingLeft - paddingRight;
        mVisibleHeight = mHeight - paddingBottom - paddingTop - bottomBar - headerBar;

       calculateLineCount();
    }

    //改变字体大小
    public void changeFontSize(int fontSize) {
        this.m_fontSize = MyUtils.sp2px(this.mContext, fontSize);
        mPaint.setTextSize(m_fontSize);
    }

    private void calculateLineCount() {

        mLineCount = (int) Math.floor((double) (mVisibleHeight / m_fontSize)) ;// 可显示的行数
        mColumCount = (int) (mVisibleWidth / m_fontSize);
    }


    public List<String> splitHtmlToPages(String url, String html, Book book) {

        html = (String) HtmlAnalysisService.analysisHtmlData(url, html, HtmlAnalysisService.AnalisysType.ContentHtml, book.BookName);
        String[] paragraphs = html.split("\n");
        List<String> pages = new ArrayList<String>();

        String tempPageContent = "";

        int tempLineCount = 0;

        for (int p = 0; p < paragraphs.length; p++) {

            String str = paragraphs[p];

            float width = 0;
            String line = "";

            char[] words = str.toCharArray();

            for (int m = 0; m < words.length; m++) {

                char word = words[m];
                line += word + "";

                if (line.length() == mColumCount || m == words.length - 1) {

                    tempPageContent += line + "\n";
                    line = "";
                    tempLineCount++;
                    if (tempLineCount == mLineCount) {
                        pages.add(tempPageContent);
                        tempPageContent = "";
                        tempLineCount = 0;
                    }
                }
            }

            if (p == paragraphs.length - 1 && !TextUtils.isEmpty(tempPageContent)) {
                pages.add(tempPageContent);
            }
        }
        return pages;
    }

}
