package com.ruobin.sodu.Util;

import com.ruobin.sodu.Interface.IHtmlRequestResult;
import com.ruobin.sodu.MyApplication;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ruobin on 2017/9/26.
 */
public class HttpHelper {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {

        if (retrofit == null) {

            retrofit = new Retrofit.Builder().baseUrl("http://www.sodu.cc/").client(genericClient()).build();
        }
        return retrofit;
    }


    public static void getMethod(String url, final Callback<ResponseBody> callback) {

        Retrofit retrofit = getRetrofit();
        ProjectAPI projectAPI = retrofit.create(ProjectAPI.class);
        Call<ResponseBody> call = projectAPI.get(url);
        call.enqueue(callback);
    }

    public static void postMethod(String url, Map<String, String> map, final Callback<ResponseBody> callback) {

        //指定客户端
        Retrofit retrofit = getRetrofit();

        ProjectAPI projectAPI = retrofit.create(ProjectAPI.class);

        Call<ResponseBody> call = projectAPI.post(url, map);

        call.enqueue(callback);
    }


    public static OkHttpClient genericClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor(MyApplication.getContextObject()));
        builder.addInterceptor(new SaveCookiesInterceptor(MyApplication.getContextObject()));
        builder.connectTimeout(12, TimeUnit.SECONDS);

        OkHttpClient httpClient = builder
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                              //  .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }

    public static void getHtmlByUrl(final String url, final IHtmlRequestResult result) {

        HttpHelper.getMethod(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                   // String html = response.body().string();

                    byte[] b = response.body().bytes(); //获取数据的bytes
                    String html = new String(b, url.contains("sodu") ? "UTF-8" : "GBK" ); //然后将其转为gb2312

                    result.success(html);

                } catch (Exception e) {
                    e.printStackTrace();
                    result.error();
                }finally {
                    result.end();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try{
                    result.error();
                }
                finally {
                    result.end();
                }
            }
        });
    }

    public static void getPostData(String url, Map<String, String> map, final IHtmlRequestResult result) {


        HttpHelper.postMethod(url, map, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    String html = response.body().string();
                    result.success(html);

                } catch (Exception e) {
                    e.printStackTrace();
                    result.error();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                result.error();
            }
        });
    }
}


