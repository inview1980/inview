package my_manage.password_box.page;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import my_manage.adapter_page.PwdPageAdapter;
import my_manage.password_box.R;
import my_manage.password_box.pojo.UserItem;
import my_manage.tool.PageUtils;
import my_manage.tool.database.DbHelper;


public final class PasswordManageViewPagerHome extends AppCompatActivity  {
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

        initPage();
    }

    private void initPage() {
        Bundle bundle      = getIntent().getExtras();
        int    currentItem = bundle.getInt("currentItem", 0);
        final int passwordTypeId = bundle.getInt("PasswordTypeId", 0);
        String title=bundle.getString("title");
        PageUtils.Log("currentItem:" + currentItem);
        if (-1 == currentItem) {
            //==-1时，为新增
            viewPage.setAdapter(new PwdPageAdapter(getSupportFragmentManager(), null,title));
            viewPage.setCurrentItem(0);
            tabTitle.addTab(tabTitle.newTab().setText(R.string.new_cn));
        } else {
            List<UserItem> userLst = DbHelper.getInstance().getItemsByAfter(this, passwordTypeId);
            initTabLayout(userLst);
            viewPage.setAdapter(new PwdPageAdapter(getSupportFragmentManager(), userLst,title));
            viewPage.setCurrentItem(currentItem);
            tabTitle.getTabAt(currentItem).select();
        }
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
}
