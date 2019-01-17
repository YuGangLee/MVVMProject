package com.lee.mvvmproject;

import android.os.Bundle;
import android.view.View;

import com.lee.mvvm.base.view.BaseActivity;
import com.lee.mvvm.liveeventbus.LiveEventBus;
import com.lee.mvvmproject.databinding.ActivityMainBinding;

import java.util.Random;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    protected int bindView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        LiveEventBus.get().with("test", String.class).observe(this, (s) -> vm.data.setValue(s));
    }

    @Override
    protected MainViewModel initVM() {
        return getDefaultVMInstance(MainViewModel.class);
    }

    @Override
    protected void loadData(Bundle dataFromIntent) {
        binding.setVm(vm);
    }

    public void testLiveData(View view) {
        Random random = new Random();
        vm.data.setValue("Random-->" + String.valueOf(random.nextInt()));
    }
}
