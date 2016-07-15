package com.archer.truesure.user.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.components.IconSelectWindow;
import com.archer.truesure.user.UserPres;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.view.CircularImageView;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户中心
 */
public class AccountActivity extends MvpActivity<AccountView, AccountPresenter> implements AccountView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_userIcon)
    CircularImageView ivUserIcon;
    @Bind(R.id.user_name)
    TextView userName;
    @Bind(R.id.linearLayout)
    RelativeLayout linearLayout;
    private ActivityUtils activityUtils;

    private IconSelectWindow iconSelectWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUtils = new ActivityUtils(this);
        setContentView(R.layout.activity_account);


    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getTitle());
    }

    /**
     * 实现MvpActivity的创建方法
     *
     * @return
     */
    @NonNull
    @Override
    public AccountPresenter createPresenter() {
        return new AccountPresenter();
    }

    /**
     * 头像图片的点击事件，从相册或者相机获取图片再更换图片，网络上传图片
     */
    @OnClick(R.id.iv_userIcon)
    public void userIcon() {

        if (iconSelectWindow == null) {
            iconSelectWindow = new IconSelectWindow(this, listener);
        }

        if (iconSelectWindow.isShowing()) {
            iconSelectWindow.dismiss();
            return;
        }

        iconSelectWindow.show();

    }

    private IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {

        /**
         * 打开相机，获取裁剪后的图片
         */
        @Override
        public void openCamera() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            startActivityForResult(CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri),
                    CropHelper.REQUEST_CAMERA);
        }

        /**
         * 在相册中找到相应图片
         */
        @Override
        public void openGallery() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            startActivityForResult(CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams()),
                    CropHelper.REQUEST_CROP);
        }

    };



    private CropHandler cropHandler = new CropHandler() {

        /**
         * 剪切完成
         * @param uri
         */
        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            activityUtils.showToast(file.getPath());

            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
            ivUserIcon.setImageBitmap(bitmap);

            /**
             * 上传图片
             */
            getPresenter().upload(getContext(),file);

        }

        /**
         * 剪切取消
         */
        @Override
        public void onCropCancel() {
            activityUtils.showToast("onCropCancel");
        }

        /**
         * 剪切失败
         * @param message
         */
        @Override
        public void onCropFailed(String message) {
            activityUtils.showToast("onCropFailed");
        }

        @Override
        public CropParams getCropParams() {
            CropParams cropParams = new CropParams();
            cropParams.aspectX = 300;
            cropParams.aspectY = 300;
            return cropParams;
        }

        @Override
        public Activity getContext() {
            return AccountActivity.this;
        }

    };

    @Override
    protected void onStart() {
        super.onStart();
        ImageLoader.getInstance().displayImage(UserPres.getString(UserPres.HEAD_PIC_URL),ivUserIcon);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
//        if (cropHandler != null) {
//            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
//        }
        super.onDestroy();
    }

    //*********************AccountView接口的实现方法**********************************

    private ProgressDialog progressDialog;

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "正在上传，请稍等...");
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        activityUtils.showToast(msg);
    }

    @Override
    public void updatePhoto(String uri) {
        ImageLoader.getInstance().displayImage(uri,ivUserIcon);
    }
}
