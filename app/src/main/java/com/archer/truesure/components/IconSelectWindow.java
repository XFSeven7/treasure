package com.archer.truesure.components;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.archer.truesure.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: qixuefeng on 2016/7/14 0014.
 * E-mail: 377289596@qq.com
 */
public class IconSelectWindow extends PopupWindow {

    private Activity activity;

    public interface Listener {
        void openCamera();

        void openGallery();
    }

    private Listener listener;

    public IconSelectWindow(Activity activity, Listener listener) {
        super(activity.getLayoutInflater().inflate(R.layout.window_select_icon, null),
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.activity = activity;
        this.listener = listener;
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());

        ButterKnife.bind(this, getContentView());

    }

    public void show() {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.btn_gallery, R.id.btn_camera, R.id.btn_cancel})
    public void doIt(View v) {

        switch (v.getId()) {
            case R.id.btn_gallery:
                listener.openGallery();
                break;
            case R.id.btn_camera:
                listener.openCamera();
                break;
        }

        dismiss();

    }


}
