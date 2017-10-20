package com.ruobin.sodu.View.Tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.DBHelper.BookCacheDao;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Model.MenuMessageEvent;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.UpdateCatalogActivity;
import com.ruobin.sodu.Util.HttpHelper;
import com.ruobin.sodu.Util.MyUtils;
import com.ruobin.sodu.View.MenuPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class Tab_OnlineShelf extends BaseTabFragment {

    public Tab_OnlineShelf Instance = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setId(R.layout.fragment_tab_online_shelf, R.layout.item_online, false, BookCacheDao.BookCacheType.OnlineShelf);
        Instance = this;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        Button btn = (Button) currentView.findViewById(R.id.btn_set_all_had_read);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllBookHadRead();
            }
        });
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

            @Override
            public void end() {

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

            if (oldBook != null) {
                if (oldBook.LastReadCatalogName != null && !newBook.NewestCatalogName.equals(oldBook.LastReadCatalogName)) {
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
            TextView tx = (TextView) currentView.findViewById(R.id.tab_online_title);
            StringBuilder stringBuilder = new StringBuilder("在线书架 ")
                    .append("(")
                    .append(books.size())
                    .append(")");

            tx.setText(stringBuilder.toString());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MenuMessageEvent event) {
        if (event.eventType == MenuMessageEvent.EventType.SetOnlineHadRead) {
            Book book = (Book) event.data;
            setBookHadRead(book);
        } else if (event.eventType == MenuMessageEvent.EventType.RemoverOnline) {
            Book book = (Book) event.data;
            removeOnlineItem(book);
        } else if (event.eventType == MenuMessageEvent.EventType.AddOnline) {
            Book book = (Book) event.data;
            addItemToOnline(book);
        }
    }


    private void removeOnlineItem(final Book book) {
        setLoadingIndicatorViewVisiablity(true);
        String url = SoDuUrl.removeOnlineBookPage + book.BookId;
        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {
                if (html.contains("取消收藏成功")) {
                    setLoadingIndicatorViewVisiablity(false);
                    mAdapter.deleteItem(book);
                    dao.deleteBookByTypeAndId(BookCacheDao.BookCacheType.OnlineShelf, book.BookId);
                    updatePageTitle();
                    Toast.makeText(currentView.getContext(), book.BookName + "取消收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    error();
                }
            }

            @Override
            public void error() {
                setLoadingIndicatorViewVisiablity(true);
                Toast.makeText(currentView.getContext(), book.BookName + "取消收藏失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void end() {

            }
        });
    }

    private void addItemToOnline(Book book) {
        setLoadingIndicatorViewVisiablity(true);
        final Book temp = book.clone();
        String url = SoDuUrl.addToShelfPage + book.BookId;
        HttpHelper.getHtmlByUrl(url, new IHtmlRequestResult() {
            @Override
            public void success(String html) {
                if ((html.contains("{\"success\":true}"))) {
                    setLoadingIndicatorViewVisiablity(false);
                    mAdapter.addNewItem(temp);
                    dao.insertOrUpdate(BookCacheDao.BookCacheType.OnlineShelf, temp);
                    updatePageTitle();
                    Toast.makeText(currentView.getContext(), temp.BookName + "添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    error();
                }
            }

            @Override
            public void error() {
                setLoadingIndicatorViewVisiablity(true);
                Toast.makeText(currentView.getContext(), temp.BookName + "添加至在线书架失败，请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void end() {

            }
        });
    }

    private void setBookHadRead(Book book) {
        book.IsNew = false;
        book.LastReadCatalogName = book.NewestCatalogName;
        dao.insertOrUpdate(BookCacheDao.BookCacheType.OnlineShelf, book);
        mAdapter.upddateItem(book);
    }

    private void setAllBookHadRead() {
        for (Book b : books) {
            if (b.IsNew) {
                setBookHadRead(b);
            }
        }
    }

    @Override
    public void itemClick(View view, int position) {

        if (books == null || books.size() <= 0) {
            return;
        }
        // Toast.makeText(getActivity(), "click " + position + " item", Toast.LENGTH_SHORT).show();
        Book book = books.get(position);
        if (book.IsNew) {
            setBookHadRead(book);
        }
        Intent intent = new Intent();
        //设置Intent的class属性，跳转到SecondActivity
        intent.setClass(this.getActivity(), UpdateCatalogActivity.class);
        //为intent添加额外的信息
        intent.putExtra("book", book);
        //启动Activity
        startActivity(intent);
    }

    @Override
    public void itemLongClick(View v, int position) {
        Book book = books.get(position);
        MenuPopupWindow popupView = new MenuPopupWindow(getActityty(), R.layout.popup_online, book);
        popupView.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
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


    private void setLoadingIndicatorViewVisiablity(boolean isVisiable) {
        View indicator = currentView.findViewById(R.id.loading_indicator);
        indicator.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
    }

}
