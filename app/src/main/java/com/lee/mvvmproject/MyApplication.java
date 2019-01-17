package com.lee.mvvmproject;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Lee.
 * Date : 2019/1/17.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
