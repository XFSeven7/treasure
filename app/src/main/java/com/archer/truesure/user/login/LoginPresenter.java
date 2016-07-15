package com.archer.truesure.user.login;

import android.content.Context;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.user.UserApi;
import com.archer.truesure.user.UserPres;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 具体的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class LoginPresenter extends MvpNullObjectBasePresenter<LoginView> {

    private Call<LoginResultInfo> loginCall;
    private Context context;

    public void login(Context context, UserInfo userInfo) {

        this.context = context;

        getView().showProgress();
        UserApi userApi = NetOkHttpClient.getInstance().getUserApi();

        if (loginCall != null) {
            loginCall.cancel();
        }

        loginCall = userApi.login(userInfo);
        loginCall.enqueue(callback);

    }

    private Callback<LoginResultInfo> callback = new Callback<LoginResultInfo>() {
        @Override
        public void onResponse(Call<LoginResultInfo> call, Response<LoginResultInfo> response) {

            getView().hideProgress();

            if (response.isSuccessful()) {

                LoginResultInfo resultInfo = response.body();
                getView().showMessage(resultInfo.getMsg());

                if (resultInfo.getCode() == 1) {
                    getView().NavigationToHome();
                    UserPres.saveInt(UserPres.TOKEN_ID, resultInfo.getTokenId());
                    return;
                }

            }

            getView().showMessage("未知错误");

        }

        @Override
        public void onFailure(Call<LoginResultInfo> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (loginCall != null) {
            loginCall.cancel();
        }

    }
}
