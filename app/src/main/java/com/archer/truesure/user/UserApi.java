package com.archer.truesure.user;

import com.archer.truesure.user.account.Update;
import com.archer.truesure.user.account.UpdateResult;
import com.archer.truesure.user.account.UploadResult;
import com.archer.truesure.user.login.LoginResultInfo;
import com.archer.truesure.user.login.UserInfo;
import com.archer.truesure.user.register.RegisterInfo;
import com.archer.truesure.user.register.RegisterResultInfo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 *
 * Created by Administrator on 2016/7/14 0014.
 */
public interface UserApi {

    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResultInfo> login(@Body UserInfo userInfo);

    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResultInfo> register(@Body RegisterInfo registerInfo);

    /**
     * 多部分上传头像
     * @param part
     * @return
     */
    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upload(@Part MultipartBody.Part part);

    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdateResult> update(@Body Update update);



}
