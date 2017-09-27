package com.ruobin.sodu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruobin.sodu.URL.SoDuUrl;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab_LocalShelf.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab_LocalShelf#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab_LocalShelf extends BaseTabFragment {

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("aa===","Tab_LocalShelf销毁");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (currentView == null) {
            currentView = inflater.inflate(R.layout.fragment_tab_local_shelf, container, false);
            //在这里做一些初始化处理

        } else {
            ViewGroup viewGroup = (ViewGroup) currentView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(currentView);
        }
        return currentView;
    }

    @Override
    public void onFragmentVisible() {

        if (!ifNeedLoadData()) {
            return;
        }

    }

    @Override
    public void onFragmentUnVisible() {

    }

    @Override
    public void setData(String html) {

        TextView txt = (TextView) this.getView().findViewById(R.id.txt_rank_content_hot);
        txt.setText(html);

    }


    @Override
    public void onRequestFailure() {


    }

    @Override
    public void itemClick(View view, int position) {

    }

    @Override
    public void itemLongClick(View view, int position) {

    }

    @Override
    public void itemInitData(View view, Object item) {

    }




}
