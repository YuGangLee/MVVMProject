package com.lee.mvvmproject;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.View;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.lee.mvvm.base.view.BaseActivity;
import com.lee.mvvmproject.databinding.ActivityMainBinding;

import java.util.Random;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {
    Test test;

    @Override
    protected int bindView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected MainViewModel initVM() {
        return getDefaultVMInstance(MainViewModel.class);
    }

    @Override
    protected void loadData(Bundle dataFromIntent) {
        binding.setVm(vm);
        test = new Test();
        test.resume();
    }

    boolean aBoolean;

    public void testLiveData(View view) {
        Random random = new Random();
        test.create();
//        startActivity(new Intent(this, TestActivity.class));
        LiveEventBus.get().with("test", String.class).post("Random-->" + String.valueOf(random.nextInt()));
        aBoolean = true;
        test.resume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aBoolean) {
            test.resume();
        }
    }

    class Test implements LifecycleOwner {

        LifecycleRegistry registry;

        Test() {
            registry = new LifecycleRegistry(this);
            init();
            create();
            resume();
            LiveEventBus.get()
                    .with("test", String.class)
                    .observe(Test.this, (s) -> vm.data.setValue(s));
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return registry;
        }

        void init() {
            registry.markState(Lifecycle.State.INITIALIZED);
        }

        void create() {
            registry.markState(Lifecycle.State.CREATED);
        }

        void start() {
            registry.markState(Lifecycle.State.STARTED);
        }

        void resume() {
            registry.markState(Lifecycle.State.RESUMED);
        }

        void destroy() {
            registry.markState(Lifecycle.State.DESTROYED);
        }
    }
}
