package com.archer.truesure.user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: qixuefeng on 2016/7/15 0015.
 * E-mail: 377289596@qq.com
 */
public class UserPres {

    public static final String USER_INFO = "user_info";

    public static final String TOKEN_ID = "token_id";
    public static final String HEAD_PIC_URL = "head_pic_url";

    private static SharedPreferences preferences;

    public static void init(Context context) {
        preferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }

    public static void saveInt(String key,int value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(key, value).apply();
    }

    public static void saveString(String key,String value) {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value).apply();
    }

    public static int getInt(String key) {
        return preferences.getInt(key, -1);
    }

    public static String getString(String key) {
        return preferences.getString(key, null);
    }

}
