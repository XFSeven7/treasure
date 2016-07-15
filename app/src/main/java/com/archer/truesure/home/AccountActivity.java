package com.archer.truesure.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archer.truesure.R;
import com.archer.truesure.common.ActivityUtils;
import com.archer.truesure.components.IconSelectWindow;
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
public class AccountActivity extends AppCompatActivity {

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

    private static final String TAG = "AccountActivity";

    @OnClick(R.id.iv_userIcon)
    public void userIcon() {

        if (iconSelectWindow == null) {
            iconSelectWindow = new IconSelectWindow(this, listener);
        }

//        if (iconSelectWindow.isShowing()) {
//            iconSelectWindow.dismiss();
//            Log.e(TAG, "userIcon: 1" );
//            return;
//        }

        Log.e(TAG, "userIcon: 2");
        iconSelectWindow.show();

    }

    private IconSelectWindow.Listener listener = new IconSelectWindow.Listener() {
        @Override
        public void openCanmera() {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            startActivityForResult(CropHelper.buildCaptureIntent(cropHandler.getCropParams().uri),
                    CropHelper.REQUEST_CAMERA);
        }

        @Override
        public void openGallay() {

            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
            startActivityForResult(CropHelper.buildCropFromGalleryIntent(cropHandler.getCropParams()),
                    CropHelper.REQUEST_CROP);

        }
    };


    private CropHandler cropHandler = new CropHandler() {

        @Override
        public void onPhotoCropped(Uri uri) {
            File file = new File(uri.getPath());
            activityUtils.showToast(file.getPath());

            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
            ivUserIcon.setImageBitmap(bitmap);

        }

        @Override
        public void onCropCancel() {
            activityUtils.showToast("onCropCancel");
        }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        CropHelper.handleResult(cropHandler, requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        if (cropHandler != null) {
            CropHelper.clearCachedCropFile(cropHandler.getCropParams().uri);
        }
        super.onDestroy();
    }
}
