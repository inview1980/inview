package com.example.mymanageclient.ui.password;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.example.mymanageclient.R;
import com.example.mymanageclient.iface.IShowList;
import com.example.mymanageclient.tool.HttpUtils;
import com.example.mymanageclient.tool.MenuUtils;
import com.example.mymanageclient.tool.PageUtils;
import com.example.mymanageclient.ui.widght.MyBaseSwipeBackActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import pojo.UserItem;
import web.WebResult;


public final class PasswordManageActivity extends MyBaseSwipeBackActivity implements IShowList {
    @BindView(R.id.main_ListViewId) SwipeMenuListView listView;
    @BindView(R.id.main_viewId)     RelativeLayout    mainViewId;
    private                         int               passwordTypeId;
    private                         boolean           isInit = true;
    private List<UserItem> items=new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_listview);
        ButterKnife.bind(this);

        passwordTypeId = getIntent().getIntExtra("PasswordTypeId", 1);
        final String title = getIntent().getStringExtra("title");

        //初始化Toolbar控件
        setTitle(title);

        initListViewSwipeMenu();
    }

    private void initListViewSwipeMenu() {
        SwipeMenuCreator creator = menu -> {
            // create "删除项目" item
            PageUtils.getSwipeMenuItem(this, menu, "删除", Color.rgb(0xF9, 0x3F, 0x25), R.drawable.ic_delete_black_24dp);
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener((position, menu, index) -> {
            if (index == 0) {// delete
                // TODO: 2020/7/3
//                PasswordManageActivityListener.deleteItem(PasswordManageActivity.this, itemList.get(position));
            }
            return false;
        });
    }

    /**
     * listView单击事件
     */
    @OnItemClick(R.id.main_ListViewId)
    void OnItemClickListener(AdapterView<?> adv, View v, int position, long l) {
        Intent intent = new Intent(this, PasswordManageViewPagerHomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("currentItem", position);
        bundle.putString("UserItems",new Gson().toJson(this.items));
        bundle.putInt("passwordTypeId",passwordTypeId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        //显示菜单中的图标
        MenuUtils.setIconVisibe(featureId, menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pwd_manage_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        PasswordManageActivityListener.onOptionsItemSelected(this, item.getItemId(), title);
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        PageUtils.checkUser(this, time, isInit, PageUtils.State.PasswordDBAddedOrDeleted, PageUtils.State.PasswordDBModified);

        isInit = false;
    }


    /**
     * 重新载入列表
     */
    @SuppressLint("CheckResult")
    @Override
    public void showList() {
        HttpUtils.getUserItems(this,passwordTypeId, this::initListView);
//        HttpUtils.get(this, getString(R.string.baseURL) + "/pwd/items"
//                +"?type="+passwordTypeId        //增加参数
//                , webResult -> {
//            if (webResult.getState() == WebResult.OK) {
//                initListView((List<UserItem>)webResult.getData());
//            }
//        }, new UserItem());
    }


    private void initListView(List<UserItem> items) {
        this.items=items;
        listView.setAdapter(new CommonAdapter<UserItem>(this, R.layout.password_manage_list_item, items) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, UserItem item, int position) {
                helper.setText(R.id.pwd_ItemId, item.getItemName())
                        .setText(R.id.pwd_AddressId, item.getAddress())
                        .setText(R.id.pwd_UserNameId, item.getUserName())
                        .setText(R.id.pwd_PasswordId, item.getPassword());
            }
        });
    }
}
