package com.archer.truesure.user.account;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Author: qixuefeng on 2016/7/15 0015.
 * E-mail: 377289596@qq.com
 */
public interface AccountView extends MvpView {

    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void updatePhoto(String url);

}
