package com.example.mymanageclient.ui.widght;

import androidx.appcompat.widget.Toolbar;

import com.example.mymanageclient.R;


public class MyBaseSwipeBackActivity  extends ParallaxSwipeBackActivity  {

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v->finish());
    }

    protected void setTitle(String title){
        ((Toolbar)findViewById(R.id.toolbar)).setTitle(title);
    }

    protected void setSubtitle(String subtitle){
        ((Toolbar)findViewById(R.id.toolbar)).setSubtitle(subtitle);
    }
}
