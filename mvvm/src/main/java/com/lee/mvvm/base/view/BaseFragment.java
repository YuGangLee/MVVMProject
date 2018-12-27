package com.lee.mvvm.base.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.mvvm.base.constract.IBaseView;
import com.lee.mvvm.base.constract.IBaseViewModel;
import com.lee.mvvm.utils.LifeCycleHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends IBaseViewModel> extends RxFragment
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
            binding.setLifecycleOwner(this);
            baseView = binding.getRoot();
            initView();
            vm = initVM();
            if (vm != null) {
                vm.attacheView();
            }
            loadData(getArguments());
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

    @Override
    public void onDestroy() {
        if (vm != null) {
            vm.viewDetached();
        }
        super.onDestroy();
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
