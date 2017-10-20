package com.ruobin.sodu.View.PageReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Model.BookCatalog;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.HtmlAnalysisService;
import com.ruobin.sodu.Util.HttpHelper;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ScanViewAdapter {
    Context context;
    List<String> items;
    AssetManager am;

    public boolean isLoading = false;

    Book book;

    BookCatalog currentCatalog;

    BookCatalog preCatalog;

    BookCatalog nextCatalog;

    Map<String, List<String>> catalogCache;


    public ScanViewAdapter(Context context, Book b) {
        this.context = context;
        this.book = b;
        am = context.getAssets();


        BookCatalog catalog = new BookCatalog();
        catalog.BookId = book.BookId;
        catalog.CatalogUrl = book.LastReadCatalogUrl;
        catalog.CatalogName = book.LastReadCatalogName;


        currentCatalog = catalog;


//        loadCatalogData(catalog, new IHtmlRequestResult() {
//            @Override
//            public void success(String html) {
//
//                items = splitHtmlToPages(html);
//
//            }
//
//            @Override
//            public void error() {
//
//            }
//
//            @Override
//            public void end() {
//
//            }
//        });
    }


    public void startLoad() {
        isLoading = true;
    }

    public void endLoading() {
        isLoading = false;
    }

    public void addContent(View view, int position) {
        TextView content = (TextView) view.findViewById(R.id.page_content);
        TextView catalog = (TextView) view.findViewById(R.id.page_catalog_name);

        if (position < 1) {
            if (isFirstCatalog()) {
                Toast.makeText(context, "没有上一条目录", Toast.LENGTH_SHORT).show();
            } else {

                //  List<String> items = getItemsByCatalog(preCatalog);
                content.setText(items.get(getCount() - 1));
                catalog.setText(currentCatalog.CatalogName);
            }
        } else if (position > getCount()) {

            if (isLastCatalog()) {
                Toast.makeText(context, "没有下一条目录", Toast.LENGTH_SHORT).show();
            } else {

                //  List<String> items = getItemsByCatalog(preCatalog);
                content.setText(items.get(0));
                catalog.setText(currentCatalog.CatalogName);
            }
        } else {

            content.setText(items.get(position - 1));
            catalog.setText(currentCatalog.CatalogName);
        }
    }

    public int getCount() {
        return items.size();
    }


    public View getView() {
        View view = LayoutInflater.from(context).inflate(R.layout.page_layout,
                null);
        return view;
    }


    public boolean isLastCatalog() {

        return nextCatalog == null;

    }

    public boolean isFirstCatalog() {

        return preCatalog == null;

    }


    public List<String> getItemsByCatalog(BookCatalog catalog) {

        if (catalogCache.containsKey(catalog.CatalogUrl)) {
            return catalogCache.get(catalog.CatalogUrl);
        }
        return null;
    }


    public List<String> splitHtmlToPages(String url, String html) {
        html = (String) HtmlAnalysisService.analysisHtmlData(url, html, HtmlAnalysisService.AnalisysType.ContentHtml, book.BookName);

        List<String> pages = new ArrayList<String>();
        for (int i = 0; i < 8; i++)
            pages.add("页面索引" + (i + 1) + "页" + html);

        return pages;
    }


    public void loadCatalogData(BookCatalog catalog, IHtmlRequestResult request) {

        String url = catalog.CatalogUrl;
        if (TextUtils.isEmpty(url)) {
            request.error();
            return;
        }
        HttpHelper.getHtmlByUrl(url, request);
    }


    private void getHtmlByCatalog(BookCatalog catalog) {

        String url = catalog.CatalogUrl;

        if (TextUtils.isEmpty(url)) {
            return;
        }

        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {


            }

            @Override
            public void error() {


            }

            @Override
            public void end() {

            }
        });
    }

}
