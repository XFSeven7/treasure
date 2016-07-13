package com.archer.truesure.temp;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class OKDemo {

    public void a() {

        OkHttpClient okHttpClient = new OkHttpClient();

        RequestBody body = new RequestBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {

            }
        };

        Request request = new Request.Builder()
                .url("sss")
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    public void shw() {

        OkHttpClient okHttpClient = new OkHttpClient();

        MediaType mediaType = MediaType.parse("treasure");
        RequestBody body = RequestBody.create(mediaType, "content");

        Request request = new Request.Builder()
                .url("")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);

        try {
            Response execute = call.execute();
            String s = execute.body().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
