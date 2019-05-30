package com.lee.mvvmproject

import android.os.Bundle
import android.view.View
import com.lee.mvvm.base.view.BaseActivity
import com.lee.mvvm.utils.ThreadHelper
import com.lee.mvvm.utils.Toaster
import com.lee.mvvmproject.databinding.ActivityMainBinding
import kotlin.random.Random

/**
 * Created by lee.
 * Date : 19-5-28.
 */

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override fun bindView() = R.layout.activity_main

    override fun initView() {

    }

    override fun initVM() = getDefaultVMInstance(MainViewModel::class.java)

    override fun loadData(dataFromIntent: Bundle?) {
        binding.vm = vm
    }

    fun testLiveData(v: View) {
        vm.data.value = "test" + Random.nextInt()
    }

}
