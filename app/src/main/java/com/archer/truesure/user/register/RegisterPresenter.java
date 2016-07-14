package com.archer.truesure.user.register;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.user.UserApi;
import com.archer.truesure.user.entity.RegisterInfo;
import com.archer.truesure.user.entity.RegisterResultInfo;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 注册界面的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class RegisterPresenter extends MvpNullObjectBasePresenter<RegisterView> {

    //******************************使用异步实现注册*************************************************

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

    //************************使用handler实现注册****************************************************

//    private Gson gson;
//
//    private Handler mHandler = new Handler(Looper.getMainLooper());
//
//    private NetOkHttpClient netOkHttpClient;
//
//    public void register(RegisterInfo info) {
//
//        gson = new Gson();
//
//        netOkHttpClient = NetOkHttpClient.getInstance();
//
//        OkHttpClient okHttpClient = netOkHttpClient.getOkHttpClient();
//
//        String s = gson.toJson(info);
//
//        RequestBody body = RequestBody.create(null, s);
//
//        final Request request = new Request.Builder()
//                .url(NetOkHttpClient.APP_URL + "/Handler/UserHandler.ashx?action=register")
//                .post(body)
//                .build();
//
//
//        Call call = okHttpClient.newCall(request);
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                failure(e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                RegisterResultInfo registerResultInfo;
//                if (response.isSuccessful()) {
//                    String string = response.body().string();
//                    registerResultInfo = gson.fromJson(string, RegisterResultInfo.class);
//                    if (registerResultInfo.getCode() == 1) {
//                        success(registerResultInfo.getMsg());
//                        return;
//                    }
//                    failure(registerResultInfo.getMsg());
//                    return;
//                }
//                failure("未知错误");
//
//            }
//        });
//
//    }
//
//    private void success(final String msg) {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                getView().hideProgress();
//                getView().showMessage(msg);
//                getView().NavigationToHome();
//            }
//        });
//
//    }
//
//    private void failure(final String msg) {
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                getView().hideProgress();
//                getView().showMessage(msg);
//            }
//        });
//
//    }

    //*********************************使用retrofit实现注册******************************************


    private Call<RegisterResultInfo> registerCall;

    public void register(RegisterInfo info) {

        Retrofit retrofit = NetOkHttpClient.getInstance().getRetrofit();

        UserApi userApi = retrofit.create(UserApi.class);
        registerCall = userApi.register(info);

        registerCall.enqueue(callback);

    }

    private Callback<RegisterResultInfo> callback = new Callback<RegisterResultInfo>() {
        @Override
        public void onResponse(Call<RegisterResultInfo> call, Response<RegisterResultInfo> response) {

            getView().hideProgress();

            if (response.isSuccessful()) {

                RegisterResultInfo body = response.body();

                getView().showMessage(body.getMsg());

                if (body.getCode() == 1) {
                    getView().NavigationToHome();
                }
                return;
            }

            getView().showMessage("未知错误");

        }

        @Override
        public void onFailure(Call<RegisterResultInfo> call, Throwable t) {
            getView().showMessage(t.getMessage());
        }
    };

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (registerCall != null) {
            registerCall.cancel();
        }

    }
}
