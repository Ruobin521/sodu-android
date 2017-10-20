package com.ruobin.sodu.View.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruobin.sodu.Model.MenuMessageEvent;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.SettingService;

import org.greenrobot.eventbus.EventBus;

public class PersonCenterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        initView();
    }

    private void initView() {
        String userName = SettingService.getInstance().getValue(SettingService.SettingOption.UserName.name(), "");
        TextView textView = (TextView)findViewById(R.id.txt_login_user);
        textView.setText(userName);

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.navigation_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button logoutBtn = (Button) findViewById(R.id.btn_logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogout();
            }
        });
    }

    private void onLogout() {
        onBackPressed();
//        Intent intent = new Intent();
//        intent.setAction("com.ruobin.login");
//        intent.putExtra("data", "logout");
//        this.sendOrderedBroadcast(intent, null);

        EventBus.getDefault().post(new MenuMessageEvent(MenuMessageEvent.EventType.Logout,null));

    }
}
