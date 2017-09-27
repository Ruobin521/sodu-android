package com.ruobin.sodu.Util;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
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
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")
                                //  .addHeader("Accept-Encoding", "gzip")
                                .addHeader("Connection", "keep-alive")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
                                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                                .addHeader("Cookie", "sodu_user=iUtPHS0jQNCLWt3WQ0ozqw==;")
                                .build();
                        return chain.proceed(request);
                    }

                })
                .build();

        return httpClient;
    }

}


