package com.archer.truesure.user.account;

import android.content.Context;

import com.archer.truesure.net.NetOkHttpClient;
import com.archer.truesure.user.UserApi;
import com.archer.truesure.user.UserPres;
import com.hannesdorfmann.mosby.mvp.MvpNullObjectBasePresenter;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author: qixuefeng on 2016/7/15 0015.
 * E-mail: 377289596@qq.com
 */
public class AccountPresenter extends MvpNullObjectBasePresenter<AccountView> {

    private String url;
    private Context context;
    private File file;

    private Call<UploadResult> uploadCall;
    private Call<UpdateResult> updateCall;

    /**
     * 上传头像
     */
    public void upload(Context context, File file) {

        this.context = context;
        this.file = file;

        /**
         * 显示加载中
         */
        getView().showProgress();

        UserApi userApi = NetOkHttpClient.getInstance().getUserApi();

        RequestBody body = RequestBody.create(null, file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image", "photo.png", body);

        if (uploadCall != null) {
            uploadCall.cancel();
        }

        uploadCall = userApi.upload(part);

        uploadCall.enqueue(callback);

    }

    private Callback<UploadResult> callback = new Callback<UploadResult>() {

        @Override
        public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {

            if (response != null && response.isSuccessful()) {

                UploadResult info = response.body();

                if (info == null) {
                    getView().showMessage("未知错误");
                    return;
                }

                if (info.getCode() != 1) {
                    getView().showMessage(info.getMsg() + "hahahahahah");
                    return;
                }

                getView().showMessage(info.getMsg());
                //运行到此处表示头像上传成功 /UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0_1.png
                url = info.getUrl();

                //加载图片
                getView().updatePhoto(NetOkHttpClient.APP_URL + url);

                String sub = url.substring(url.lastIndexOf("/") + 1, url.length());
                UserPres.saveString(UserPres.HEAD_PIC_URL, NetOkHttpClient.APP_URL + url);

                UserApi userApi = NetOkHttpClient.getInstance().getUserApi();

                int tokenid = UserPres.getInt(UserPres.TOKEN_ID);

                Call<UpdateResult> update = userApi.update(new Update(tokenid, sub));
                update.enqueue(updateResultCallback);

            }

            getView().showMessage("未知错误");

        }

        @Override
        public void onFailure(Call<UploadResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    private Callback<UpdateResult> updateResultCallback = new Callback<UpdateResult>() {
        @Override
        public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {

            if (response != null && response.isSuccessful()) {

                UpdateResult body = response.body();

                if (body == null) {
                    getView().showMessage("未知错误");
                    return;
                }

                getView().showMessage(body.getMsg());

            }

            getView().hideProgress();

        }

        @Override
        public void onFailure(Call<UpdateResult> call, Throwable t) {
            getView().hideProgress();
            getView().showMessage(t.getMessage());
        }
    };

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (uploadCall != null) {
            uploadCall.cancel();
        }
        if (updateCall != null) {
            updateCall.cancel();
        }
    }
}
