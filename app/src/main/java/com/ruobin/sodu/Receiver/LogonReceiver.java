package com.ruobin.sodu.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ruobin.sodu.MainActivity;

/**
 * Created by ruobin on 2017/10/9.
 */
public class LogonReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String data = intent.getStringExtra("data");

        if (data.equals("login")) {
            MainActivity.Instance.setLogon(true);

        } else if (data.equals("logout")) {
            MainActivity.Instance.setLogon(false);
        }

        abortBroadcast();
    }
}
