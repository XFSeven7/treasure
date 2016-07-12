package com.archer.truesure.user.register;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public interface RegisterView {

    void showProgress();

    void hideProgress();

    void showMessage(String msg);

    void NavigationToHome();

    /**
     * 注册账号
     */
    void addRegister();

}
