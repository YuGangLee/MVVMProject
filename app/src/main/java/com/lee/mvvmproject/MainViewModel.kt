package com.lee.mvvmproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class MainViewModel : ViewModel() {

    val data = MutableLiveData<String>()
}
