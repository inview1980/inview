package com.example.mymanageclient;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymanageclient.ui.password.PasswordManageTotalActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
    @BindView(R.id.main_topTxt)  TextView    mainTopTxt;
    @BindView(R.id.main_pwdBtn)  ImageButton mainPwdBtn;
    @BindView(R.id.main_timeBtn) ImageButton mainTimeBtn;
    @BindView(R.id.version)      TextView    versionTxt;
    @BindView(R.id.loading)      ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        versionTxt.setText("版本号:" + getVersionName(this));
//        val ss=FontUtils.getInstance().getSpannableString(versionTxt,"&#xe64e;加载默认数据到数据库");
//        ss.setSpan(new RelativeSizeSpan(1.5f),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        versionTxt.setText(ss);

//        Intent intent = new Intent(this, MyService.class);
//        intent.putExtra("path", DBFilePath);
//        startService(intent);
    }

@OnClick({R.id.main_pwdBtn,R.id.main_timeBtn})
    public void mainBtn_onClick(View view) {
        int btn = view.getId();//获取按键的值
        if (btn == R.id.main_pwdBtn) {
            startActivity(new Intent(this, PasswordManageTotalActivity.class));
        }

        if (btn == R.id.main_timeBtn) {
//            Intent intent = new Intent();
//            intent.setClass(MainActivity.this, RentalMainActivity.class);
//            startActivity(intent);
        }
    }


    public String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String         name    = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) { e.printStackTrace(); }
        return name;
    }

    @OnLongClick(R.id.version)
    public boolean onLongClick(View view) {
//        PopupMenu menu = new PopupMenu(this, view);
//        menu.getMenuInflater().inflate(R.menu.main_menu, menu.getMenu());
//        menu.setOnMenuItemClickListener(this);
//        menu.show();
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//            case R.id.loadDefault2DB:
//                if (DbHelper.getInstance().loadDefault2DB(this))
//                    PageUtils.showMessage(this, "读取默认数据库成功");
//                else
//                    PageUtils.showMessage(this, "读取默认数据库失败");
//                break;
//            case R.id.loadFile2DB:
//                loadFile();
//                break;
//            default:
//                break;
//        }
        return false;
    }
}
