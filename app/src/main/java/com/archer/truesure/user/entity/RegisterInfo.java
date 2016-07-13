package com.archer.truesure.user.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class RegisterInfo {

//    "Password":"654321"
//    "UserName":"qjd"

    @SerializedName("Password")
    private String passWord;

    @SerializedName("UserName")
    private String userName;

    public RegisterInfo(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getUserName() {
        return userName;
    }
}
