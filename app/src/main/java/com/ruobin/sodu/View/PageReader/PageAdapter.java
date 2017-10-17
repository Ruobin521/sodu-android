package com.ruobin.sodu.View.PageReader;

import android.view.View;

/**
 * Created by ds on 2017/10/17.
 */

public abstract class PageAdapter
{

    public abstract View getView();

    public abstract int getCount();

    public abstract void addContent(View view, int position);
}
