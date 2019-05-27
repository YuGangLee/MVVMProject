package com.lee.mvvmproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class MainViewModel extends ViewModel {

    public final MutableLiveData<String> data = new MutableLiveData<>();

}
