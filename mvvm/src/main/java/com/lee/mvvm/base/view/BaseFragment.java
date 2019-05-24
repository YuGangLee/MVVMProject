package com.lee.mvvm.base.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.mvvm.base.view.intf.IBaseView;
import com.lee.mvvm.utils.LifeCycleHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends ViewModel> extends RxFragment
        implements IBaseView {

    private View baseView;
    protected B binding;
    protected VM vm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (baseView == null) {
            binding = DataBindingUtil.inflate(inflater, bindView(), container, false);
            baseView = binding.getRoot();
            initView();
            vm = initVM();
            loadData(getArguments());
            binding.setLifecycleOwner(this);
        } else if (container != null) {
            ViewGroup viewGroup = (ViewGroup) container.getParent();
            viewGroup.removeView(this.baseView);
        }
        return baseView;
    }

    @Override
    public LifeCycleHelper getDefaultLifeCycle() {
        return new LifeCycleHelper(this);
    }

    protected VM getDefaultVMInstance(Class<VM> vmClass) {
        if (getActivity() != null) {
            return ViewModelProviders.of(getActivity()).get(getActivity().getClass().getName(), vmClass);
        }
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
}
