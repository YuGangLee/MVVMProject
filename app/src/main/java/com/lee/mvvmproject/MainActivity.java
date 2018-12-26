package com.lee.mvvmproject;

import android.os.Bundle;

import com.lee.mvvm.base.constract.IBaseViewModel;
import com.lee.mvvm.base.view.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected int bindView() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected IBaseViewModel initVM() {
        return null;
    }

    @Override
    protected void loadData(Bundle dataFromIntent) {

    }
}
