package com.archer.truesure.net;

import com.archer.truesure.user.UserApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络连接
 * 包含okHttp和retrofit
 * Created by Administrator on 2016/7/13 0013.
 */
public class NetOkHttpClient {

    public static final String APP_URL = "http://admin.syfeicuiedu.com";

    private OkHttpClient okHttpClient;

    private static NetOkHttpClient netOkHttpClient;

    private Retrofit retrofit;

    private UserApi userApi;

    private NetOkHttpClient() {

        //非严格模式
        Gson gson = new GsonBuilder().setLenient().create();

        okHttpClient = new OkHttpClient();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                //添加转换器
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APP_URL)
                .build();
    }

    public static NetOkHttpClient getInstance() {
        if (netOkHttpClient == null) {
            synchronized (NetOkHttpClient.class) {
                if (netOkHttpClient == null) {
                    netOkHttpClient = new NetOkHttpClient();
                }
            }
        }
        return netOkHttpClient;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public UserApi getUserApi() {
        if (userApi == null) {
            userApi = retrofit.create(UserApi.class);
        }
        return userApi;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
