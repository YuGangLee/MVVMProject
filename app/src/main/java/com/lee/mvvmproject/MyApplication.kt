package com.lee.mvvmproject

import android.app.Application
import com.lee.mvvm.utils.AppUtils

import com.squareup.leakcanary.LeakCanary

/**
 * Created by Lee.
 * Date : 2019/1/17.
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
        AppUtils.init(this)
    }
}
