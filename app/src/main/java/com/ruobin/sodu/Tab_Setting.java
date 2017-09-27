package com.ruobin.sodu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
        return inflater.inflate(R.layout.fragment_tab_setting, container, false);
    }


}
