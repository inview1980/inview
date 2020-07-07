package com.example.mymanageclient.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.example.mymanageclient.R;
import com.example.mymanageclient.iface.IShowList;
import com.example.mymanageclient.ui.login.LoginActivity;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @author inview
 * @Date 2020/6/29 11:17
 * @Description :
 */
public class PageUtils {
    public static final String TAG = "inview";

    private static boolean isDebug = true;


    /**
     * 当前为debug时记录信息,生产环境不记录
     */
    public static void Log(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    /**
     * 当前为debug时记录信息,生产环境不记录
     */
    public static void LogError(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static void isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            isDebug = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            isDebug = false;
        }
    }

    /**
     * 弹出Toast信息，并根据debug状态，记录相关信息
     */
    public static void showMessage(Context context, String msg) {
        Log(msg);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 输入框失去焦点则关闭
     */
    public static void closeInput(Activity activity, boolean b) {
        if (!b) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)//软键盘如果打开就关闭
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 设置所有EditText、TextView中的文字下划线
     */
    public static void setUnderline(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (final Field field : fields) {
                field.setAccessible(true);
                Object obj = field.get(object);
                if (field.getType() == EditText.class) {
                    ((EditText) obj).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    ((EditText) obj).getPaint().setAntiAlias(true);//抗锯齿

                } else if (field.getType() == TextView.class) {
                    ((TextView) obj).getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    ((TextView) obj).getPaint().setAntiAlias(true);//抗锯齿
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射，将本类中控制所有的EditText、CheckBox、TextView、Spinner控件的setEnable方法
     */
    public static void setEnable(Object obj, boolean b) {
        try {
            Field[]  fields = obj.getClass().getDeclaredFields();
            Class<?> clazz  = Class.forName("android.view.View");
            for (final Field field : fields) {
                if (field.getType() == EditText.class || field.getType() == CheckBox.class
                        || field.getType() == Spinner.class) {
                    Method method = clazz.getMethod("setEnabled", boolean.class);
                    field.setAccessible(true);
                    method.invoke(field.get(obj), b);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static void getSwipeMenuItem(Context context, SwipeMenu menu, String title, int color, int iconId) {
        // create  item
        SwipeMenuItem item = new SwipeMenuItem(context);
        item.setBackground(new ColorDrawable(color));
        item.setWidth(dp2px(context, 70));
        item.setTitle(title);
        item.setTitleSize(18);
        item.setTitleColor(Color.WHITE);
        // set a icon
        item.setIcon(iconId);
        // add to menu
        menu.addMenuItem(item);
    }

    private static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }


    /**
     * 给TabLayout控件增加OnTabSelectedListener事件，在点击标签时，可以转到相应的页
     *
     * @param tab
     * @param viewPage
     */
    public static void addOnTabSelectedListenerByTabLayout(TabLayout tab, ViewPager viewPage) {
        tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPage) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                viewPage.setCurrentItem(tab.getPosition());
            }
        });
    }

    /**
     * 如果超过时间，要求输入密码；如果数据已变更，重新载入数据
     * @param activity
     * @param time
     * @param isInit
     * @param passwordDBAddedOrDeleted
     * @param passwordDBModified
     * @param <T>
     */
    public static <T extends Activity & IShowList> void checkUser(T activity, long time, boolean isInit, boolean passwordDBAddedOrDeleted, boolean passwordDBModified) {
        long t = activity.getResources().getInteger(R.integer.LockIntervalByMinute) * 60 * 1000;
//        long t = 10 * 1000;
        if (time == 0 || (new Date().getTime() - time) < t) {
            //如果初始化或数据已改变执行
            if (isInit || PageUtils.State.PasswordDBAddedOrDeleted) {
                activity.showList();
            }
        } else {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.putExtra("isLogin", false);
            activity.startActivityForResult(intent, 8080);
        }
    }

    /**
     * 标识数据库的修改状态
     */
    public static class State {
        /**
         * 密码数据内容已修改
         */
        public static boolean PasswordDBModified       = false;
        /**
         * 密码数据已增删
         */
        public static boolean PasswordDBAddedOrDeleted = false;
        /**
         * 房租数据内容已修改
         */
        public static boolean RentalDBModified         = false;
        /**
         * 房租数据已增删
         */
        public static boolean RentalDBAddedOrDeleted   = false;
    }
}
