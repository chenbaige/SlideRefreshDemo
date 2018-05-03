package com.hbandroid.sliderefreshdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hbandroid.sliderefreshdemo.view.StickyNavLayout;

public class MainActivity extends AppCompatActivity implements StickyNavLayout.OnStickStateChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void isStick(boolean isStick) {

    }

    @Override
    public void scrollPercent(float percent) {

    }
}
