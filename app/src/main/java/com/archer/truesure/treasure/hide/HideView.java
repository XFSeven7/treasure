package com.archer.truesure.treasure.hide;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Author: qixuefeng on 2016/7/21 0021.
 * E-mail: 377289596@qq.com
 */
public interface HideView extends MvpView {

    void showProgress();
    void hideProgress();
    void showMessage(String msg);
    void navigateToHome();

}
