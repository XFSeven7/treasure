package com.archer.truesure.user.account;

import com.google.gson.annotations.SerializedName;

/**
 * Author: qixuefeng on 2016/7/15 0015.
 * E-mail: 377289596@qq.com
 */
public class UploadResult {


//    errcode : '文件系统上传成功！',                  //返回信息
//    urlcount: 1,                                   //返回值
//    imgUrl:
//            '/UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0.png',
//    smallImgUrl:
//            '/UpLoad/HeadPic/f683f88dc9d14b648ad5fcba6c6bc840_0_1.png'  //头像地址

    @SerializedName("errcode")
    private String msg;

    @SerializedName("urlcount")
    private int code;

    @SerializedName("smallImgUrl")
    private String url;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

}
