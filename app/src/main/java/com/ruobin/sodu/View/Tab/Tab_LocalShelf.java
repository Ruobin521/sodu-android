package com.ruobin.sodu.View.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.ruobin.sodu.SearchActivity;
import com.ruobin.sodu.Service.ListDataAnalysisService;
import com.ruobin.sodu.UpdateCatalogActivity;
import com.ruobin.sodu.Util.HttpHelper;
import com.ruobin.sodu.View.MenuPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class Tab_LocalShelf extends BaseTabFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setId(R.layout.fragment_tab_local_shelf, R.layout.item_local, false, BookCacheDao.BookCacheType.LocalShelf);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    public void loadData() {
        super.loadData();
        String url = SoDuUrl.home;

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
    public void initUI() {
        Button btnUpdate = (Button) currentView.findViewById(R.id.btn_local_shelf_all_update);
        if (btnUpdate == null) {
            return;
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "检查所有的更新", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setData(String html) {

        if (html == null || html == "") {
            return;
        }

        List<Book> list = ListDataAnalysisService.AnalysisHotRecommendDatas(html);
        books = list;
        super.updateUI();
        super.endLoad();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MenuMessageEvent event) {
        if (event.eventType == MenuMessageEvent.EventType.RemoveLoacal) {
            Book book = (Book) event.data;
            deleteLoaclItem(book);
        } else if (event.eventType == MenuMessageEvent.EventType.SetLocalHadRead) {
            Book book = (Book) event.data;
            setBookHadRead(book);
        } else if (event.eventType == MenuMessageEvent.EventType.AddLocal) {
            Book book = ((Book) event.data).clone();
            addLoacalItem(book);
        } else if (event.eventType == MenuMessageEvent.EventType.CheckLocalUpdate) {
            Book book = (Book) event.data;
            checkItemUpdate(book);
        }
    }

    private void deleteLoaclItem(Book book) {
        mAdapter.deleteItem(book);
    }

    private void setBookHadRead(Book book) {
        book.IsNew = false;
        book.LastReadCatalogName = book.NewestCatalogName;
        dao.insertOrUpdate(BookCacheDao.BookCacheType.LocalShelf, book);
        mAdapter.upddateItem(book);
    }

    private void addLoacalItem(Book book) {
        dao.insertOrUpdate(BookCacheDao.BookCacheType.LocalShelf, book);
        mAdapter.addNewItem(book);
    }

    private void checkItemUpdate(Book book) {
        Toast.makeText(getContext(), "正在检查更新，请稍后", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClick(View view, int position) {
        Book book = books.get(position);
        Intent intent = new Intent();
        intent.setClass(this.getActivity(), UpdateCatalogActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    @Override
    public void itemLongClick(View view, int position) {
        Book book = books.get(position);
        MenuPopupWindow popupView = new MenuPopupWindow(getActivity(), R.layout.popup_local, book);
        popupView.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void itemInitData(View view, Object item) {
        Book book = (Book) item;
        if (book == null) {
            return;
        }
        TextView tv = (TextView) view.findViewById(R.id.item_local_book_name);
        tv.setText(((Book) item).BookName);

        TextView tv2 = (TextView) view.findViewById(R.id.item_local_new_chapter_name);
        tv2.setText(((Book) item).NewestCatalogName);

        TextView tv3 = (TextView) view.findViewById(R.id.item_local_old_chapter_name);
        tv3.setText(((Book) item).LastReadCatalogName);

        ImageView img = (ImageView) view.findViewById(R.id.item_local_img_new);
        img.setVisibility(book.IsNew ? View.VISIBLE : View.GONE);
    }


}
