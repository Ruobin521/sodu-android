package com.ruobin.sodu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ruobin.sodu.Constants.SoDuUrl;
import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.Util.HttpHelper;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    FrameLayout loading;
    FrameLayout loginMain;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }


    private void initView() {

        loading = (FrameLayout)findViewById(R.id.loading);
        loginMain = (FrameLayout)findViewById(R.id.login_main);

        LinearLayout backBtn = (LinearLayout) findViewById(R.id.navigation_bar_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLogin();
            }
        });
    }


    private void onLogin() {

        EditText txtName = (EditText) findViewById(R.id.txt_login_user);
        EditText txtPassWd = (EditText) findViewById(R.id.txt_login_password);

        if (TextUtils.isEmpty(txtName.getText().toString()) || TextUtils.isEmpty(txtPassWd.getText().toString())) {

            Toast.makeText(this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
            return;
        }

        loading.setVisibility(View.VISIBLE);
        loginMain.setEnabled(false);

        String url = SoDuUrl.loginPostPage;
        Map<String, String> postData = new HashMap<>();
        postData.put("username", txtName.getText().toString());
        postData.put("userpass", txtPassWd.getText().toString());

        HttpHelper.getPostData(url, postData, new IHtmlRequestResult() {
            @Override
            public void success(String html) {

                if (html != null && html.contains("{\"success\":true}")) {
                    onLoginResult(true,"");
                } else {
                    onLoginResult(false,"登录失败,请检测用户名密码是否正确");
                }
            }

            @Override
            public void error() {
                onLoginResult(false,"登录失败,请检测网络连接或服务是否正常");
            }
        });
    }

    public void onLoginResult(boolean isSuccess,String info) {

        loading.setVisibility(View.GONE);
        loginMain.setEnabled(true);

        if (isSuccess) {

            onBackPressed();

            Intent intent = new Intent();
            intent.setAction("com.ruobin.login");
            intent.putExtra("data", "login");
            this.sendOrderedBroadcast(intent,null);

        } else {

            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
        }
    }
}
