package com.example.mymanageclient.ui.login;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.mymanageclient.R;
import com.example.mymanageclient.tool.SecretUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author inview
 * @Date 2020/6/30 17:18
 * @Description :
 */
@RunWith(AndroidJUnit4.class)
public class LoginActivityTest  {
//    @Rule
//    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class);
//
////    @Before
//    public void setUp() throws Exception {
//    }
//
//    @Test
//    public void login_button() throws Exception {
//        SecretUtil.Password="3";
//        SecretUtil.UserName="3";
//        onView(withId(R.id.username))
//                .perform(ViewActions.clearText(), ViewActions.replaceText("3"));
//        onView(withId(R.id.password))
//                .perform(ViewActions.clearText(), ViewActions.replaceText("444444"));
//
//        onView(withId(R.id.login))
//                .perform(ViewActions.click());
//    }
    @Test
    public void test(){

    }
}