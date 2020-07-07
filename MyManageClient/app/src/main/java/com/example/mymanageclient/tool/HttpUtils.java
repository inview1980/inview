package com.example.mymanageclient.tool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.core.util.Consumer;

import com.example.mymanageclient.R;
import com.example.mymanageclient.ui.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import pojo.IconDetails;
import pojo.UserItem;
import web.WebResult;

/**
 * @author inview
 * @Date 2020/6/29 10:24
 * @Description :
 */
public class HttpUtils {


    /**
     * get文本数据
     */
    @SuppressLint("CheckResult")
    public static void get(Activity activity, String urlString, Consumer<WebResult> doSomething, Object obj) {
        OkGo.<String>get(urlString)
                .tag(activity)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<String>())
                .observeOn(AndroidSchedulers.mainThread())//
                .subscribe(res -> {
                    try {
                        WebResult webResult = unzip2WebResult(res, obj);
                        if (webResult.getState() == WebResult.NEED_LOGIN) {
                            /** 登录，要求返回值 */
                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.putExtra("isLogin", true);
                            activity.startActivityForResult(intent, 8080);
                        } else {
                            doSomething.accept(webResult);
                        }
                    } catch (Exception e) {
                        PageUtils.LogError(e.getLocalizedMessage());
                    }
                });
    }

    /**
     * 将接收到的数据转换
     *
     * @param res
     * @param t
     * @param <T>
     * @return
     */
    private static <T> WebResult<List<T>> unzip2WebResult(Response<String> res, T t) {
        WebResult<List<T>> webResult = null;

        String str;
        Gson   gson = new Gson();
        if (WebResult.UZIP.equals(res.headers().get(WebResult.UZIP))) {
            //压缩后的数据，需解压
            str = UZip.gunzip(res.body());
        } else str = res.body();
        if (t.getClass() == IconDetails.class)
            webResult = gson.fromJson(str, new TypeToken<WebResult<List<IconDetails>>>() {
            }.getType());
        if (t.getClass() == UserItem.class)
            webResult = gson.fromJson(str, new TypeToken<WebResult<List<UserItem>>>() {
            }.getType());
        return webResult;
    }

    /**
     * 获取密码项目的具体数据，并将密码解密
     * @param activity
     * @param passwordTypeId
     * @param items
     */
    public static void getUserItems(Activity activity, int passwordTypeId, Consumer<List<UserItem>> items) {
        HttpUtils.get(activity, activity.getString(R.string.baseURL) + "/pwd/items"
                        + "?type=" + passwordTypeId        //增加参数
                , webResult -> {
                    if (webResult.getState() == WebResult.OK) {
                        List<UserItem> userLst = (List<UserItem>) webResult.getData();
                        items.accept(userLst);
                    }
                }, new UserItem());
    }
}
