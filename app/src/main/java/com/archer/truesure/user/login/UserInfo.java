package com.archer.truesure.user.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class UserInfo {

//        "UserName":"qjd",
//        "Password":"654321"

    @SerializedName("UserName")
    private String userName;
    @SerializedName("Password")
    private String passWord;

    public UserInfo(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }
}
