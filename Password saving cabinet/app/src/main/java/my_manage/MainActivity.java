package my_manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

import my_manage.password_box.R;
import my_manage.password_box.page.dialog.Login_Activity;
import my_manage.rent_manage.MyService;
import my_manage.rent_manage.RentalMainActivity;
import my_manage.tool.ContentUriUtil;
import my_manage.tool.PageUtils;
import my_manage.tool.database.DbBase;
import my_manage.tool.database.DbHelper;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
    public static String   DBFilePath;
    private       TextView versionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化数据库
        dbInit();

        versionTxt = findViewById(R.id.version);
        versionTxt.setText("版本号:" + getVersionName(this));
        versionTxt.setOnLongClickListener(this);
//        val ss=FontUtils.getInstance().getSpannableString(versionTxt,"&#xe64e;加载默认数据到数据库");
//        ss.setSpan(new RelativeSizeSpan(1.5f),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        versionTxt.setText(ss);

        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("path", DBFilePath);
        startService(intent);
    }


    public void mainBtn_onClick(View view) {
        int btn = view.getId();//获取按键的值
        if (btn == R.id.main_pwdBtn) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Login_Activity.class);
            startActivity(intent);
        }

        if (btn == R.id.main_timeBtn) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, RentalMainActivity.class);
            startActivity(intent);
        }
    }

    private void dbInit() {
        //初始化数据库
        DBFilePath = getFilesDir().getAbsolutePath() + "/" + getString(R.string.rentalFileName);
        DbBase.createCascadeDB(this, DBFilePath);
        //初始化系统参数
        PageUtils.isApkInDebug(this);
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

    @Override
    public boolean onLongClick(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.getMenuInflater().inflate(R.menu.main_menu, menu.getMenu());
        menu.setOnMenuItemClickListener(this);
        menu.show();
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.loadDefault2DB:
                if (DbHelper.getInstance().loadDefault2DB(this))
                    PageUtils.showMessage(this, "读取默认数据库成功");
                else
                    PageUtils.showMessage(this, "读取默认数据库失败");
                break;
            case R.id.loadFile2DB:
                loadFile();
                break;
            default:
                break;
        }
        return false;
    }

    private void loadFile() {
        String path = getExternalFilesDir(null).getAbsolutePath()
                + "/" + getString(R.string.rentalFileNameBackup) + "."
                + getString(R.string.extensionName);
        File   file   = new File(path);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(this, "my_manage.password_box.fileprovider", file);
            intent.setDataAndType(uri, "*/*");
        } else
            intent.setType("*/*");//文件类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String title = "请选择一个指定后缀名的数据库文件：" + getString(R.string.extensionName);
        startActivityForResult(Intent.createChooser(intent, title), 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 111 && resultCode == Activity.RESULT_OK) {
            if (data.getData() != null) {//只有一个文件咯
                Uri    uri  = data.getData(); // 获取用户选择文件的URI
                String path = ContentUriUtil.getPath(this, uri);
                //排除非本应用的指定后缀名的文件
                if (null != path && !path.endsWith(getString(R.string.extensionName))) {
                    PageUtils.showMessage(this, "请选择指定后缀名的文件");
                    return;
                }
                if (DbHelper.getInstance().loadFile2DB(path)) {
                    PageUtils.showMessage(this, "读取指定文件并写入数据库成功");
                } else
                    PageUtils.showMessage(this, "读取指定失败");
            }
        }
    }
}
