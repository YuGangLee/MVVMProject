package com.lee.mvvm.base.view;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lee.mvvm.base.view.intf.IBaseView;
import com.lee.mvvm.utils.LifeCycleHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

public abstract class BaseLazyFragment<B extends ViewDataBinding, VM extends ViewModel> extends RxFragment
        implements IBaseView {

    private View baseView;

    private View lazeView;

    protected B binding;

    protected VM vm;

    boolean loading;

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
            try {
                lazeView = inflater.inflate(setLazyView(), container, false);
                ((ViewGroup) baseView).addView(lazeView);
            } catch (Exception ignored) {
                Log.w("BaseLazyFragment", "onCreateView: no lazy view");
            }
        } else if (container != null) {
            ViewGroup viewGroup = (ViewGroup) container.getParent();
            viewGroup.removeView(this.baseView);
        }
        loading = true;
        lazyLoad();
        return baseView;
    }

    @Override
    public LifeCycleHelper getDefaultLifeCycle() {
        return new LifeCycleHelper(this);
    }

    protected VM getDefaultVMInstance(Class<VM> vmClass) {
        return ViewModelProviders.of(this).get(getClass().getName(), vmClass);
    }

    private void lazyLoad() {
        if (getUserVisibleHint() && loading) {
            initView();
            vm = initVM();
            loadData(getArguments());
            binding.setLifecycleOwner(this);
            loading = false;
            if (lazeView != null && baseView != null) {
                ((ViewGroup) baseView).removeView(lazeView);
            }
        }
    }

    /**
     * 获取到databinding将要绑定的布局Id
     *
     * @return layoutId
     */
    @LayoutRes
    protected abstract int bindView();

    /**
     * 获取懒加载时的替换布局Id
     *
     * @return layoutId
     */
    @LayoutRes
    protected abstract int setLazyView();

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
