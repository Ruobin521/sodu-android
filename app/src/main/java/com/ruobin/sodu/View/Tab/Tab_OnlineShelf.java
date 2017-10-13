package com.ruobin.sodu.View.Tab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.DBHelper.BookCacheDao;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.UpdateCatalogActivity;
import com.ruobin.sodu.Util.HttpHelper;

import java.util.List;


public class Tab_OnlineShelf extends BaseTabFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setId(R.layout.fragment_tab_online_shelf, R.layout.item_online, false, BookCacheDao.BookCacheType.OnlineShelf);

    }


    private Activity getActityty() {

        return getActivity();
    }

    public void loadData() {
        super.loadData();
        String url = SoDuUrl.bookShelfPage;

        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {
                setData(html);
            }

            @Override
            public void error() {
                onRequestFailure();
            }
        });
    }



    @Override
    public void setData(String html) {
        if (html == null || html == "") {
            return;
        }
        List<Book> list = ListDataAnalysisService.AnalysisOnlineShelfDatas(html);

        if (books != null && books.size() > 0) {
            compareBooks(books, list);
        }
        books = list;
        updateUI();
        super.endLoad();
    }


    private void compareBooks(List<Book> oldBooks, List<Book> newBooks) {

        for (Book newBook : newBooks) {

            Book oldBook = selectBook(oldBooks, newBook.BookId);

            if (newBook != null) {
                if (oldBook.LastReadCatalogName!= null && !newBook.NewestCatalogName.equals(oldBook.LastReadCatalogName)) {
                    newBook.LastReadCatalogName = oldBook.LastReadCatalogName;
                    newBook.IsNew = true;
                } else {
                    newBook.LastReadCatalogName = newBook.NewestCatalogName;
                    newBook.IsNew = false;
                }
            }
        }
    }


    private Book selectBook(List<Book> books, String bookId) {
        Book selectedBook = null;
        for (Book book : books) {
            if (book.BookId.equals(bookId)) {
                selectedBook = book;
                break;
            }
        }
        return selectedBook;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        updatePageTitle();

    }

    private void updatePageTitle() {
        if (books != null) {
            TextView tx = (TextView) this.getView().findViewById(R.id.tab_online_title);
            StringBuilder stringBuilder = new StringBuilder("在线书架 ")
                    .append("(")
                    .append(books.size())
                    .append(")");

            tx.setText(stringBuilder.toString());
        }
    }

    @Override
    public void itemClick(View view, int position) {

        // Toast.makeText(getActivity(), "click " + position + " item", Toast.LENGTH_SHORT).show();
        Book book = books.get(position);

        if(book.IsNew) {
            book.IsNew = false;
            ImageView img = (ImageView) view.findViewById(R.id.item_online_img_new);
            img.setVisibility(View.GONE);
        }
        dao.insertOrUpdate(book, BookCacheDao.BookCacheType.OnlineShelf);

        Intent intent = new Intent();
        //设置Intent的class属性，跳转到SecondActivity
        intent.setClass(this.getActivity(), UpdateCatalogActivity.class);
        //为intent添加额外的信息
        intent.putExtra("book", book);
        //启动Activity
        startActivity(intent);

    }

    @Override
    public void itemLongClick(View view, int position) {

    }

    @Override
    public void itemInitData(View view, Object item) {

        Book book = (Book) item;

        TextView tv = (TextView) view.findViewById(R.id.item_online_book_name);
        tv.setText(book.BookName);

        TextView tv2 = (TextView) view.findViewById(R.id.item_online_new_chapter_name);
        tv2.setText(book.NewestCatalogName);

        TextView tv3 = (TextView) view.findViewById(R.id.item_online_update_time);
        tv3.setText(book.UpdateTime);

        TextView tv4 = (TextView) view.findViewById(R.id.item_online_old_chapter_name);
        tv4.setText(book.LastReadCatalogName);

        ImageView img = (ImageView) view.findViewById(R.id.item_online_img_new);
        img.setVisibility(book.IsNew ? View.VISIBLE : View.GONE);
    }


}
