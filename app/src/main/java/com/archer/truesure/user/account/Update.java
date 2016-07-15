package com.archer.truesure.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Author: qixuefeng on 2016/7/15 0015.
 * E-mail: 377289596@qq.com
 */
public class Update {

//    {
//        "Tokenid":3,"
//        "HeadPic": "05a1a7e18ab940679dbd0e506be31add.jpg"
//    }

    @SerializedName("Tokenid")
    private int tokenId;

    @SerializedName("HeadPic")
    private String headPic;

    public Update(int tokenId, String headPic) {
        this.tokenId = tokenId;
        this.headPic = headPic;
    }
}
