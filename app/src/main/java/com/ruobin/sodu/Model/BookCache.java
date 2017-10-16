package com.ruobin.sodu.Model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.ruobin.sodu.Util.JsonUitl;

/**
 * Created by ds on 2017/10/13.
 */

public class BookCache {

    @DatabaseField(generatedId = true)
    public int id;

    @DatabaseField
    public String bookId;

    @DatabaseField(canBeNull = true)
    public String bookStr;

    @DatabaseField
    public int cacheType;

    public long time;

    public BookCache(Book book, int type) {
        bookId = book.BookId;
        cacheType = type;
        bookStr = JsonUitl.objectToString(book);
        time =  System.currentTimeMillis();
    }

    public BookCache() {

    }

    public  Book getBook() {
        if(TextUtils.isEmpty(bookStr)) {
            return  null;
        }
        Book book = (Book)JsonUitl.stringToObject(bookStr,Book.class);
        return  book;
    }

}
