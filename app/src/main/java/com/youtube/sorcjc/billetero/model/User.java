package com.youtube.sorcjc.billetero.model;

import android.content.Context;

import com.youtube.sorcjc.billetero.Global;

public class User {

    public static String getAuthHeader(Context context) {
        return "Bearer " + getAccessToken(context);
    }

    public static String getAccessToken(Context context) {
        return Global.getStringFromPrefs(context, "access_token");
    }


}
