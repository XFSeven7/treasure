package com.archer.truesure.net;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class NetOkHttpClient {

    public static final String APP_URL = "http://admin.syfeicuiedu.com";

    private OkHttpClient okHttpClient;

    private static NetOkHttpClient netOkHttpClient;

    private NetOkHttpClient() {
        okHttpClient = new OkHttpClient();
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

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

}
