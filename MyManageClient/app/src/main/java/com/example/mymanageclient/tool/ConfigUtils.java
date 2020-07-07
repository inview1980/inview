package com.example.mymanageclient.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import com.example.mymanageclient.R;

/**
 * @author inview
 * @Date 2020/6/30 9:02
 * @Description :
 */
public class ConfigUtils {
    public static final String ConfigFileName = "config";

    public static boolean saveLoginState(Context context, String userName, String pwd) {
        SharedPreferences        sps  = context.getSharedPreferences(ConfigFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed   = sps.edit();
        String                   salt = StrUtils.getRandomString(context.getResources().getInteger(R.integer.salt_length));
        ed.putString("userName", userName);
        ed.putString("salt", salt);
        ed.putString("password", SecretUtil.getEncryptString(salt, pwd));
        return ed.commit();
    }

    public static Pair<String, String> readLoginState(Context context) {
        SharedPreferences sps      = context.getSharedPreferences(ConfigFileName, Context.MODE_PRIVATE);
        String            salt     = sps.getString("salt", null);
        String            userName = sps.getString("userName", null);
        String            pwd      = sps.getString("password", null);
        if (StrUtils.isAnyBlank(salt, userName, pwd))
            return new Pair<>(null, null);
        try {
            String password = SecretUtil.getDecryptSting(salt, pwd);
            return new Pair<>(userName, password);
        } catch (Exception e) {
            return new Pair<>(null, null);
        }
    }
}
