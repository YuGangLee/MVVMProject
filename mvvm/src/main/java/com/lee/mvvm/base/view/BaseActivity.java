package com.lee.mvvm.base.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.lee.mvvm.base.view.intf.IBaseView;
import com.lee.mvvm.utils.LifeCycleHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends ViewModel> extends RxAppCompatActivity
        implements IBaseView {

    protected B binding;

    protected VM vm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, bindView());
        vm = initVM();
        initView();
        loadData(getIntent().getExtras());
        binding.setLifecycleOwner(this);
    }

    @Override
    public LifeCycleHelper getDefaultLifeCycle() {
        return new LifeCycleHelper(this);
    }

    protected VM getDefaultVMInstance(Class<VM> vmClass) {
        return ViewModelProviders.of(this).get(getClass().getName(), vmClass);
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
}
