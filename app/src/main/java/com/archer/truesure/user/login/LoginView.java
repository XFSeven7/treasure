package com.archer.truesure.user.login;

/**
 * 视图需要做的事情
 * Created by Administrator on 2016/7/12 0012.
 */
public interface LoginView {

    /**
     * 显示加载中
     */
    void showProgress();

    /**
     * 隐藏加载
     */
    void hideProgress();

    /**
     * 发送消息
     * @param msg 具体消息
     */
    void showMessage(String msg);

    /**
     * 跳转到主页
     */
    void NavigationToHome();



}
