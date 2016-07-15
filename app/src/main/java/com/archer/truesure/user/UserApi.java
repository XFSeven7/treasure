package com.archer.truesure.user;

import com.archer.truesure.user.entity.LoginResultInfo;
import com.archer.truesure.user.entity.RegisterInfo;
import com.archer.truesure.user.entity.RegisterResultInfo;
import com.archer.truesure.user.entity.UserInfo;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 *
 * Created by Administrator on 2016/7/14 0014.
 */
public interface UserApi {

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResultInfo> login(@Body UserInfo userInfo);

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResultInfo> register(@Body RegisterInfo registerInfo);

}
