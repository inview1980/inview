package com.example.mymanageclient.ui.widght;

import android.content.Intent;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymanageclient.tool.MenuUtils;

import java.util.Date;

/**
 * @author inview
 * @Date 2020/7/6 11:39
 * @Description :退出此页面一段时间后需要登录
 */
public class MyBaseActivity extends AppCompatActivity {
    protected long    time   = 0;
    protected boolean isInit = true;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 8080) {
            time = new Date().getTime();
        } else {
            //按退出键时
            time = 1;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        time = new Date().getTime();
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //显示菜单中的图标
        MenuUtils.setIconVisibe(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }
}
