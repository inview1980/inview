package com.example.mymanageclient.ui.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymanageclient.MainActivity;
import com.example.mymanageclient.R;
import com.example.mymanageclient.tool.ConfigUtils;
import com.example.mymanageclient.tool.PageUtils;
import com.example.mymanageclient.tool.SecretUtil;
import com.example.mymanageclient.tool.StrUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okrx2.adapter.ObservableResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import tools.SecretUtils;
import web.WebResult;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username) EditText             username;
    @BindView(R.id.password) EditText             password;
    @BindView(R.id.login)    Button               login;
    @BindView(R.id.loading)  ProgressBar          loading;
    private                  boolean              isLogin;
    private                  Pair<String, String> config;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        isLogin = getIntent().getBooleanExtra("isLogin", true);
        /** 如果未登录 */
        if (StrUtils.isAnyBlank(SecretUtil.Password, SecretUtil.UserName)) {
            /** 读取配置文件，初始化登录窗口 */
            config = ConfigUtils.readLoginState(getApplicationContext());
            if (StrUtils.isAllNotBlank(config.first, config.second)) {
                username.setText(config.first);
            }
        }
        if (!isLogin) {
            username.setText(SecretUtil.UserName == null ? "" : SecretUtil.UserName);
            username.setEnabled(false);
            password.setText("");
        }
    }

    @OnEditorAction(R.id.password)
    boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onClick(null);
        }
        return false;
    }

    @OnTextChanged({R.id.username, R.id.password})
    void afterTextChanged() {
        String psd = password.getText().toString();
        login.setEnabled(psd.trim().length() > 0);
    }

    @OnClick(R.id.login)
    public void onClick(View view) {
        login.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        if (isLogin)
            userLogin();
        else {
            if (SecretUtil.UserName.equals(username.getText().toString()) && SecretUtil.Password.equals(password.getText().toString())) {
                setResult(8080);
                finish();
            } else {
                Toast.makeText(this, "密码错误，请重新输入！", Toast.LENGTH_LONG).show();
            }
        }
        login.setEnabled(true);
        loading.setVisibility(View.GONE);
    }

    @SuppressLint("CheckResult")
    private void userLogin() {
        String url      = getString(R.string.baseURL) + "/login";
        String userName = username.getText().toString();
        String pwd      = password.getText().toString();
        OkGo.<String>post(url).params("username", userName)
                .tag(this)
                .params("password", SecretUtils.getPasswordEncryptString(pwd))
                .converter(new StringConvert())
                .adapt(new ObservableResponse<String>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    try {
                        Gson      gson      = new Gson();
                        WebResult webResult = gson.fromJson(res.body(), WebResult.class);
                        if (webResult.getState() == WebResult.OK) {
                            PageUtils.Log(String.valueOf(webResult.getDetails()));
                            /** 将用户名、密码加密后保存到配置文件 */
                            ConfigUtils.saveLoginState(getApplicationContext(), userName, pwd);
                            /** 将用户名、密码放入SecretUtil便于调用 */
                            SecretUtil.UserName = userName;
                            SecretUtil.Password = pwd;
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else if (webResult.getState() == WebResult.USER_NULL) {
                            showDialog(webResult.getDetails());
                            username.setSelected(true);
                            PageUtils.LogError(String.valueOf(webResult.getDetails()));
                        } else if (webResult.getState() == WebResult.PASSWORD_ERROR) {
                            showDialog(webResult.getDetails());
                            password.setSelected(true);
                            PageUtils.LogError(String.valueOf(webResult.getDetails()));
                        }
                        loading.setVisibility(View.GONE);
                        login.setEnabled(true);
                    } catch (Exception e) {
                        PageUtils.LogError(e.getLocalizedMessage());
                    }
                });
    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("错误");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", null).show();
    }
}
