package com.ruobin.sodu.Util;

import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ruobin on 2017/9/29.
 */

public class OnDoubleClick implements View.OnTouchListener {

    long mLastTime=0;
    long mCurTime=0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            mLastTime=mCurTime;
            mCurTime= System.currentTimeMillis();
            if(mCurTime-mLastTime<300){//双击事件
                mCurTime =0;
                mLastTime = 0;
                return  true;
            }
        }
        return false;
    }
}
