package com.ruobin.sodu.View.Tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ruobin.sodu.MainActivity;
import com.ruobin.sodu.R;
import com.ruobin.sodu.Service.LogonService;
import com.ruobin.sodu.UpdateCatalogActivity;
import com.ruobin.sodu.View.setting.LoginActivity;
import com.ruobin.sodu.View.setting.PersonCenterActivity;


public class Tab_Setting extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aa===","Tab_Setting销毁");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_setting, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        RelativeLayout personCenter = (RelativeLayout)view.findViewById(R.id.setting_person_center);
        personCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!LogonService.isLogon()) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.Instance, LoginActivity.class);
                    startActivity(intent);
                }else{
                    navigateTo(PersonCenterActivity.class);
                }
            }
        });
    }

     private void navigateTo(Class t) {
         Intent intent = new Intent();
         intent.setClass(this.getActivity(),t);
         startActivity(intent);
     }

}
