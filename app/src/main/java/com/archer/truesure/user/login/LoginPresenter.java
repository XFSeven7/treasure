package com.archer.truesure.user.login;

/**
 * 具体的业务逻辑
 * Created by Administrator on 2016/7/12 0012.
 */
public class LoginPresenter {

    private LoginView loginView;

    public LoginPresenter(LoginView loginView) {
        this.loginView = loginView;
    }

    public void login() {

        loginView.showProgress();

        /*
        连接网络{

            读取数据{
                loginView.hideProgress();
                loginView.nevigateToHome();
            }else{
                loginView.hideProgress();
                loginView.showMessage("没数据");
                return;
            }

        }else{
            loginView.hideProgress();
            loginView.showMessage("没网");
            return;
        }
        */

    }

}
