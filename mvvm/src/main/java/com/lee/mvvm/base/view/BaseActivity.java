package com.lee.mvvm.base.view;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.lee.mvvm.base.view.intf.IBaseView;
import com.lee.mvvm.liveeventbus.LiveEventBus;
import com.lee.mvvm.utils.LifeCycleHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends ViewModel> extends RxAppCompatActivity
        implements IBaseView {
    public static final String START_ACTIVITY = "com.lee.mvvm.base.view.BaseActivity:START_ACTIVITY";
    public static final String QUIT_APP = "com.lee.mvvm.base.view.BaseActivity:QUIT_APP";

    protected B binding;
    protected VM vm;
    protected boolean isStartingActivity;
    protected IInterceptor interceptor;
    protected boolean isFront;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, bindView());
        vm = initVM();
        preSet();
        initView();
        loadData(getIntent().getExtras());
        binding.setLifecycleOwner(this);
    }

    @Override
    public LifeCycleHelper getDefaultLifeCycle() {
        return new LifeCycleHelper(this);
    }

    /**
     * add status listener for StartActivity or QuitApp
     */
    protected void preSet() {
        LiveEventBus.get()
                .with(START_ACTIVITY, Bundle.class)
                .observe(this, bundle -> {
                    if (isFront && bundle != null) {
                        startActivity(getClass(), bundle);
                    }
                });
        LiveEventBus.get()
                .with(QUIT_APP)
                .observe(this, o -> finish());
    }

    protected VM getDefaultVMInstance(Class<VM> vmClass) {
        return ViewModelProviders.of(this).get(getClass().getName(), vmClass);
    }

    protected void startActivity(Class<? extends Activity> activityClass) {
        this.startActivity(activityClass, null);
    }

    protected void startActivity(Class<? extends Activity> activityClass, @Nullable Bundle data) {
        if (interceptor != null && interceptor.process() || isStartingActivity) {
            return;
        }
        isStartingActivity = true;
        Intent intent = new Intent(this, activityClass);
        if (data != null) {
            intent.putExtras(data);
        }
        runOnUiThread(() -> {
            startActivity(intent);
            isStartingActivity = false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        isFront = true;
    }

    @Override
    protected void onPause() {
        isFront = false;
        super.onPause();
    }

    /**
     * 获取到databinding将要绑定的布局Id
     *
     * @return layoutId
     */
    @LayoutRes
    protected abstract int bindView();

    /**
     * 进行可能的View初始化
     */
    protected abstract void initView();

    /**
     * 每一个View默认持有一个ViewModel
     *
     * @return VM: Nullable
     */
    protected abstract VM initVM();

    /**
     * 进行数据的初始化以及Databinding中的数据绑定
     *
     * @param dataFromIntent 从Intent中取到的数据
     */
    protected abstract void loadData(Bundle dataFromIntent);

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public interface IInterceptor {
        void init();

        boolean process();
    }
}
