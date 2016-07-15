package com.archer.truesure.user.register;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.user.UserApi;
import com.archer.truesure.user.UserPres;
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

    private Call<RegisterResultInfo> registerCall;

    public void register(RegisterInfo info) {

        getView().showProgress();

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
                    UserPres.saveInt(UserPres.TOKEN_ID,body.getTokenId());
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
