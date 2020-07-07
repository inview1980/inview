package com.example.mymanageclient.ui.password;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.mymanageclient.R;
import com.example.mymanageclient.iface.IShowList;
import com.example.mymanageclient.tool.HttpUtils;
import com.example.mymanageclient.tool.PageUtils;
import com.example.mymanageclient.ui.adapter.PwdPageAdapter;
import com.example.mymanageclient.ui.widght.MyBaseActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import pojo.UserItem;
import web.WebResult;


public final class PasswordManageViewPagerHomeActivity extends MyBaseActivity implements IShowList {
    @BindView(R.id.tab_title) TabLayout tabTitle;
    @BindView(R.id.toolbar)   Toolbar   toolbar;
    @BindView(R.id.viewPage)  ViewPager viewPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_toolbar_tablayout_viewpage);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @OnPageChange(R.id.viewPage)
    void onPageSelected(int position) {
        tabTitle.getTabAt(position).select();
    }

    private void initTabLayout(List<UserItem> userLst) {
        for (final UserItem item : userLst) {
            tabTitle.addTab(tabTitle.newTab().setText(item.getItemName()));
        }
        PageUtils.addOnTabSelectedListenerByTabLayout(tabTitle,viewPage);
    }

    @Override
    public void showList() {
        Bundle bundle      = getIntent().getExtras();
        int    currentItem = bundle.getInt("currentItem", 0);
        final String userItems = bundle.getString("UserItems" );
        final int passwordTypeId = bundle.getInt("passwordTypeId" );
        String title=bundle.getString("title");
        PageUtils.Log("currentItem:" + currentItem);
        if (-1 == currentItem) {
            //==-1时，为新增
            viewPage.setAdapter(new PwdPageAdapter(getSupportFragmentManager(), null,title));
            viewPage.setCurrentItem(0);
            tabTitle.addTab(tabTitle.newTab().setText(R.string.new_cn));
        } else {
            if(PageUtils.State.PasswordDBAddedOrDeleted||PageUtils.State.PasswordDBModified){
                //如果数据有更改，重新载入数据
                HttpUtils.getUserItems(this,passwordTypeId,items -> {
                    initViewPage(currentItem, title, items);
                });
//                HttpUtils.get(this, getString(R.string.baseURL) + "/pwd/items"
//                                +"?type="+passwordTypeId        //增加参数
//                        , webResult -> {
//                            if (webResult.getState() == WebResult.OK) {
//                                List<UserItem> userLst=(List<UserItem>)webResult.getData();
//                                initViewPage(currentItem, title, userLst);
//                            }
//                        }, new UserItem());
            }else {
                List<UserItem> userLst =new Gson().fromJson(userItems,new TypeToken<List<UserItem>>() {
                }.getType());
                initViewPage(currentItem, title, userLst);
            }
        }
    }

    private void initViewPage(int currentItem, String title, List<UserItem> userLst) {
        initTabLayout(userLst);
        viewPage.setAdapter(new PwdPageAdapter(getSupportFragmentManager(), userLst,title));
        viewPage.setCurrentItem(currentItem);
        tabTitle.getTabAt(currentItem).select();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PageUtils.checkUser(this, time, isInit, PageUtils.State.PasswordDBAddedOrDeleted, PageUtils.State.PasswordDBModified);

        isInit = false;
    }
}
