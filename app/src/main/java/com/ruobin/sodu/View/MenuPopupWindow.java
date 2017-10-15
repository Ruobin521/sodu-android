package com.ruobin.sodu.View;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Currency;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ruobin.sodu.Model.Book;
import com.ruobin.sodu.Model.MenuMessageEvent;
import com.ruobin.sodu.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ruobi on 17/10/14.
 */

public class MenuPopupWindow extends PopupWindow {
    private View conentView;
    private Book book;

    public MenuPopupWindow(Activity context, int id, Book b) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(id, null);
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态  
        this.update();
        // 实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
        this.setBackgroundDrawable(dw);
        conentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        book = b;

        initButton();
    }

    private void initButton() {
        Button btnSetHadRead = (Button) conentView.findViewById(R.id.btn_set_had_read);
        if (btnSetHadRead != null) {
            if(book.IsNew) {
                btnSetHadRead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new MenuMessageEvent(MenuMessageEvent.EventType.SetOnlineHadRead,book));
                        close();
                    }
                });
            }else{
                btnSetHadRead.setVisibility(View.GONE);
            }
        }

        Button btnRemove = (Button) conentView.findViewById(R.id.btn_remove);
        if (btnRemove != null) {
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new MenuMessageEvent(MenuMessageEvent.EventType.RemoverOnline,book));
                    close();
                }
            });
        }

        Button btnRemoveLocal = (Button) conentView.findViewById(R.id.btn_remove_local);
        if (btnRemoveLocal != null) {
            btnRemoveLocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new MenuMessageEvent(MenuMessageEvent.EventType.RemoveLoacal,book));
                    close();
                }
            });
        }



        Button btnAdd = (Button) conentView.findViewById(R.id.btn_add_online_shelf);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new MenuMessageEvent(MenuMessageEvent.EventType.AddOnline,book));
                    close();
                }
            });
        }

        TextView txtBookName = (TextView) conentView.findViewById(R.id.menu_book_name);
        txtBookName.setText(book.BookName);
    }


    public void close() {
        this.dismiss();
    }
}
