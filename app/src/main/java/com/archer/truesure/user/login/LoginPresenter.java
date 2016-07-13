package com.archer.truesure.user.login;

import android.os.AsyncTask;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.user.entity.LoginResultInfo;
import com.archer.truesure.user.entity.UserInfo;
import com.google.gson.Gson;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 具体的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

    private NetOkHttpClient netOkHttpClient;

    private Gson gson;

    private UserInfo user;

    /**
     * 模拟登录
     */
    public void login(UserInfo user) {
        this.user = user;
        gson = new Gson();
        netOkHttpClient = NetOkHttpClient.getInstance();
        new MyAsyncTask().execute();
    }

    private final class MyAsyncTask extends AsyncTask<Void, Void, LoginResultInfo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected LoginResultInfo doInBackground(Void... params) {

            OkHttpClient okHttpClient = netOkHttpClient.getOkHttpClient();

            String content = gson.toJson(user);

            MediaType type = MediaType.parse("truesure");
            RequestBody body = RequestBody.create(type, content);

            Request request = new Request.Builder()
                    .url(NetOkHttpClient.APP_URL+"/Handler/UserHandler.ashx?action=login")
                    .post(body)
                    .build();

            Call call = okHttpClient.newCall(request);

            Response execute;
            String json;
            try {
                execute = call.execute();
                json = execute.body().string();
            } catch (IOException e) {
                return null;
            }

            LoginResultInfo loginResultInfo = gson.fromJson(json, LoginResultInfo.class);

            if (loginResultInfo != null) {
                return loginResultInfo;
            }

            return null;
        }

        @Override
        protected void onPostExecute(LoginResultInfo loginResultInfo) {
            super.onPostExecute(loginResultInfo);

            getView().hideProgress();

            if (loginResultInfo == null) {
                getView().showMessage("未知错误");
                return;
            }

            /*
                "errcode": 1,
                "errmsg": "登录成功！",

                "errcode":2,
                "errmsg":"此用户已被锁住！无法正常登录！"

                "errcode": 3,
                "errmsg": "用户名不存在!请先注册成会员再登录",

                "errcode": 4,
                "errmsg": "密码错误！",

                "errcode":5,
                "errmsg":"此用户已登录"
            */

            switch (loginResultInfo.getCode()) {

                case 1:
                    getView().showMessage(loginResultInfo.getMsg());
                    getView().NavigationToHome();
                    break;

                case 2:
                case 3:
                case 4:
                    getView().showMessage(loginResultInfo.getMsg());
                    break;

            }

        }
    }

}
