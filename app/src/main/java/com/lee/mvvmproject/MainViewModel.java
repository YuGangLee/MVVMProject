package com.lee.mvvmproject;

import android.arch.lifecycle.MutableLiveData;

import com.lee.mvvm.base.view_model.BaseViewModel;

public class MainViewModel extends BaseViewModel {

    public final MutableLiveData<String> data = new MutableLiveData<>();

    @Override
    public void attacheView() {
        data.setValue("Start");
    }

    @Override
    public void onViewStop() {

    }

    @Override
    public void viewDetached() {

    }
}
