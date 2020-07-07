package com.example.mymanageclient.ui.password;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.example.mymanageclient.R;
import com.example.mymanageclient.iface.IShowList;
import com.example.mymanageclient.tool.HttpUtils;
import com.example.mymanageclient.tool.MenuUtils;
import com.example.mymanageclient.tool.PageUtils;
import com.example.mymanageclient.ui.login.LoginActivity;
import com.example.mymanageclient.ui.widght.MyBaseSwipeBackActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okrx2.adapter.ObservableResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import pojo.IconDetails;

import com.example.mymanageclient.tool.UZip;

import web.WebResult;

public final class PasswordManageTotalActivity extends MyBaseSwipeBackActivity implements IShowList {
    @BindView(R.id.content) RecyclerView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_manage_totle_activity);
        ButterKnife.bind(this);

        //初始化Toolbar控件
        setTitle(R.string.pwdManage_cn);

        content.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.password_total_item_count)));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pwd_manage_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        PasswordManageActivityListener.onOptionsItemSelected(this, item.getItemId(), "");
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PageUtils.checkUser(this, time, isInit, PageUtils.State.PasswordDBAddedOrDeleted, false);
        isInit = false;
    }

    @Override
    public void showList() {
        HttpUtils.get(this, getString(R.string.baseURL) + "/pwd/itemMenu", webResult -> {
            if (webResult.getState() == WebResult.OK) {
                initRecyclerView((List<IconDetails>) webResult.getData());
            }
        }, new IconDetails());
    }


    /**
     * 将数据解释后在ListView中显示
     *
     * @param data
     */
    private void initRecyclerView(List<IconDetails> data) {
        if (data == null) return;
        CommonRecyclerAdapter<IconDetails> adapter = new CommonRecyclerAdapter<IconDetails>(this,
                R.layout.password_manage_totle_item,
                data) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, IconDetails item, int position) {
                helper.setText(R.id.icon, Html.fromHtml(item.getIcon(), Html.FROM_HTML_MODE_COMPACT))
                        .setTextColor(R.id.icon, Color.parseColor(item.getColor()))
                        .setText(R.id.name, item.getName())
                        .setText(R.id.count, "(" + item.getCount() + ")");
            }
        };
        adapter.setOnItemClickListener((viewHolder, view, i) -> {
            if (i < 0 || i >= data.size()) return;
            IconDetails id = data.get(i);

            PageUtils.Log(id.getName() + ";id:" + id.getId());
            Intent intent = new Intent(PasswordManageTotalActivity.this, PasswordManageActivity.class);
            intent.putExtra("PasswordTypeId", id.getId());
            intent.putExtra("title", id.getName());
            startActivity(intent);
        });
        content.setAdapter(adapter);
    }
}
