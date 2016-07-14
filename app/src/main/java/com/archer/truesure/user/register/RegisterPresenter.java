package com.archer.truesure.user.register;

import android.os.Handler;
import android.os.Looper;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.user.entity.RegisterInfo;
import com.archer.truesure.user.entity.RegisterResultInfo;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 注册界面的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {


//    private NetOkHttpClient netOkHttpClient;
//
//    private Gson gson;
//
//    private RegisterInfo registerInfo;

//    /**
//     * 模拟注册
//     */

//    public void register(RegisterInfo registerInfo) {
//        this.registerInfo = registerInfo;
//        netOkHttpClient = NetOkHttpClient.getInstance();
//        gson = new Gson();
//        new MyAsyncTask().execute();
//    }
//
//    private final class MyAsyncTask extends AsyncTask<Void, Void, RegisterResultInfo> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected RegisterResultInfo doInBackground(Void... params) {
//
//            OkHttpClient okHttpClient = netOkHttpClient.getOkHttpClient();
//
//            String content = gson.toJson(registerInfo);
//
//            MediaType type = MediaType.parse("truesure");
//            RequestBody body = RequestBody.create(type, content);
//
//            Request request = new Request.Builder()
//                    .post(body)
//                    .url(NetOkHttpClient.APP_URL + "/Handler/UserHandler.ashx?action=register")
//                    .build();
//
//            Call call = okHttpClient.newCall(request);
//
//            Response execute;
//            String json;
//            try {
//                execute = call.execute();
//                json = execute.body().string();
//            } catch (IOException e) {
//                return null;
//            }
//
//            RegisterResultInfo registerResultInfo = gson.fromJson(json, RegisterResultInfo.class);
//
//            if (registerResultInfo != null) {
//                return registerResultInfo;
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(RegisterResultInfo registerResultInfo) {
//            super.onPostExecute(registerResultInfo);
//
//            getView().hideProgress();
//
//            if (registerResultInfo == null) {
//                getView().showMessage("未知错误");
//                return;
//            }
//
//            /*
//
//                "errcode": 1,                  //状态值
//                "errmsg": "登录成功！",        //返回信息
//                "tokenid": 171                 //用户令牌
//
//                "errcode":2,
//                "errmsg":"注册此用户名已存在！"
//
//            */
//
//            switch (registerResultInfo.getCode()) {
//
//                case 1:
//                    getView().showMessage(registerResultInfo.getMsg());
//                    getView().NavigationToHome();
//                    break;
//                case 2:
//                    getView().showMessage(registerResultInfo.getMsg());
//                    break;
//
//            }
//
//        }
//
//    }

    private Gson gson;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private NetOkHttpClient netOkHttpClient;

    public void register(RegisterInfo info) {

        gson = new Gson();

        netOkHttpClient = NetOkHttpClient.getInstance();

        OkHttpClient okHttpClient = netOkHttpClient.getOkHttpClient();

        String s = gson.toJson(info);

        RequestBody body = RequestBody.create(null, s);

        final Request request = new Request.Builder()
                .url(NetOkHttpClient.APP_URL + "/Handler/UserHandler.ashx?action=register")
                .post(body)
                .build();


        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                failure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                RegisterResultInfo registerResultInfo;
                if (response.isSuccessful()) {
                    String string = response.body().string();
                    registerResultInfo = gson.fromJson(string, RegisterResultInfo.class);
                    if (registerResultInfo.getCode() == 1) {
                        success(registerResultInfo.getMsg());
                        return;
                    }
                    failure(registerResultInfo.getMsg());
                    return;
                }
                failure("未知错误");

            }
        });

    }

    private void success(final String msg) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().hideProgress();
                getView().showMessage(msg);
                getView().NavigationToHome();
            }
        });

    }

    private void failure(final String msg) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                getView().hideProgress();
                getView().showMessage(msg);
            }
        });

    }

}
