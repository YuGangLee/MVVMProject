package com.lee.mvvmproject;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;


public class MainViewModel extends ViewModel {

    public final MutableLiveData<String> data = new MutableLiveData<>();

}
