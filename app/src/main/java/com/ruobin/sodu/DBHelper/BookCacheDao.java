package com.ruobin.sodu.DBHelper;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Model.BookCache;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by ds on 2017/10/13.
 */


public class BookCacheDao {

    public static enum BookCacheType {
        None(-1),
        OnlineShelf(0),
        Rank(1),
        Hot(2),
        LocalShelf(3);

        private int value;

        private BookCacheType(int v) {

            value = v;
        }

        public int getValue() {

            return value;
        }
    }

    private Dao<BookCache, Integer> bookCacheDaoOpe;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public BookCacheDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            bookCacheDaoOpe = helper.getDao(BookCache.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertOrUpdate( BookCacheType type,Book book) {
        try {
            deleteBookByTypeAndId(type,book.BookId);
            BookCache cache = new BookCache(book, type.getValue());
            bookCacheDaoOpe.create(cache);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addBooks(List<Book> books, BookCacheType type) {
        try {
            int result = deleteBooksByCacheType(type);
            for (Book book : books) {
                BookCache cache = new BookCache(book, type.getValue());
                bookCacheDaoOpe.create(cache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public Book getBookWithBookId(int id) {
        Book book = null;
        try {
            BookCache cache = bookCacheDaoOpe.queryForId(id);
            book = cache.getBook();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }


    public List<Book> getBooksByCacheType(BookCacheType type) {
        List<Book> books = null;
        try {
            int typeId = type.getValue();
            List<BookCache> caches = bookCacheDaoOpe.queryBuilder().where().eq("cacheType", typeId).query();

            if (caches != null && caches.size() > 0) {

                Collections.sort(caches,new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        if(o1 instanceof BookCache && o2 instanceof BookCache){
                            BookCache e1 = (BookCache) o1;
                            BookCache e2 = (BookCache) o2;
                            return (int)(e1.time - e2.time);
                        }
                        throw new ClassCastException("不能转换为Emp类型");
                    }
                });

                books = new ArrayList<Book>();
                for (BookCache cache : caches) {
                    books.add(cache.getBook());
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public int deleteBooksByCacheType(BookCacheType type) {
        int result = 0;
        List<Book> books = null;
        try {
            int typeId = type.getValue();
            DeleteBuilder deleteBuilder = bookCacheDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("cacheType", typeId);
            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int deleteBookByTypeAndId(BookCacheType type, String bookId) {
        int result = 0;
        try {
            int typeId = type.getValue();
            DeleteBuilder deleteBuilder = bookCacheDaoOpe.deleteBuilder();
            deleteBuilder.where().eq("cacheType", typeId).and().eq("bookId", bookId);

            result = deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
