package com.ruobin.sodu.Interface;

/**
 * Created by dang on 2017/9/27.
 */

public interface IHtmlRequestResult {

    void success(String html);

    void error();

    void end();

}
