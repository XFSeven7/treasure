package com.archer.truesure.user.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class LoginResultInfo {

//              "errcode": 1,                  //状态值
//              "errmsg": "登录成功！",        //返回信息
//              "headpic": "add.jpg",          //头像地址
//              "tokenid": 171

    @SerializedName("errcode")
    private int code;

    @SerializedName("errmsg")
    private String msg;

    @SerializedName("headpic")
    private String headPic;

    @SerializedName("tokenid")
    private int tokenId;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getHeadPic() {
        return headPic;
    }

    public int getTokenId() {
        return tokenId;
    }
}
