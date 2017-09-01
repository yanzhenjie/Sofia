package com.yanzhenjie.statusview.sample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.yanzhenjie.statusview.SystemBarHelper;

public class NormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        new SystemBarHelper.Builder()
                .enableFullToNavigationBar()
                .enableFullToStatusBar()
                .enableImmersedStatusBar(true)
                .enableImmersedNavigationBar(true)
//                .statusBarColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
//                .navigationBarColor(ContextCompat.getColor(this, android.R.color.holo_blue_bright))
                .into(this);
    }
}
