package com.archer.truesure.user.register;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class RegisterResultInfo {

//            "errcode": 1,                  //状态值
//            "errmsg": "登录成功！",        //返回信息
//            "tokenid": 171                 //用户令牌

    @SerializedName("errcode")
    private int code;

    @SerializedName("errmsg")
    private String msg;

    @SerializedName("tokenid")
    private int tokenId;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public int getTokenId() {
        return tokenId;
    }
}
