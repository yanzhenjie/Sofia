package com.yanzhenjie.statusview.sample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.yanzhenjie.statusview.SystemBarHelper;

public class NormalActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private SystemBarHelper mSystemBarHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright));
        mSystemBarHelper = new SystemBarHelper();
        mSystemBarHelper.setFullToStatusBar(this, ContextCompat.getColor(this, android.R.color.holo_blue_bright));
        mSystemBarHelper.setFullToNavigationBar(this, ContextCompat.getColor(this, android.R.color.holo_blue_bright));
    }
}
