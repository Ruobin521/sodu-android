package com.ruobin.sodu.View.PageReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Model.BookCatalog;
import com.ruobin.sodu.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ScanViewAdapter {
    Context context;
    List<String> items;
    AssetManager am;

    Book book;

    BookCatalog currentCatalog;

    BookCatalog preCatalog;

    BookCatalog nextCatalog;

    Map<String, List<String>> catalogCache;


    public ScanViewAdapter(Context context, Book b) {
        this.context = context;
        this.book = b;
        am = context.getAssets();

        items = new ArrayList<String>();
        for (int i = 0; i < 8; i++)
            items.add("页面索引" + (i + 1) + "页");
    }

    public void addContent(View view, int position) {
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView tv = (TextView) view.findViewById(R.id.index);

        if (position < 1) {
            if (isFirstCatalog()) {
                Toast.makeText(context, "没有上一条目录", Toast.LENGTH_SHORT).show();
            } else {

            //  List<String> items = getItemsByCatalog(preCatalog);
                content.setText("测试内容");
                tv.setText(items.get(getCount() - 1));
            }
        } else if(position > getCount()){

            if (isLastCatalog()) {
                Toast.makeText(context, "没有下一条目录", Toast.LENGTH_SHORT).show();
            } else {

                //  List<String> items = getItemsByCatalog(preCatalog);
                content.setText("测试内容");
                tv.setText(items.get(0));
            }
        }else{

            content.setText("测试内容");
            tv.setText(items.get(position -1 ));
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
}
