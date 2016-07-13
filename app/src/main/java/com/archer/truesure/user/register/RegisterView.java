package com.archer.truesure.user.register;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public interface RegisterView extends MvpView {

    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void NavigationToHome();

    /**
     * 注册账号
     */
    void addRegister();

}
