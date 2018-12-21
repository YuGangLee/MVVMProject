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

import org.greenrobot.eventbus.EventBus;

public abstract class BaseLazyFragment<B extends ViewDataBinding, VM extends IBaseViewModel> extends RxFragment
        implements IBaseView {

    private View baseView;

    private View lazeView;

    protected B binding;

    protected VM vm;

    boolean loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (baseView == null) {
            binding = DataBindingUtil.inflate(inflater, bindView(), container, false);
            baseView = binding.getRoot();
            lazeView = inflater.inflate(setLazyView(), container, false);
            ((ViewGroup) baseView).addView(lazeView);
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

    private void lazyLoad() {
        if (getUserVisibleHint() && loading) {
            initView();
            vm = initVM();
            if (vm != null) {
                vm.attacheView();
            }
            loadData(getArguments());
            loading = false;
            if (lazeView != null && baseView != null) {
                ((ViewGroup) baseView).removeView(lazeView);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (vm != null) {
            vm.viewDetached();
        }
        EventBus.getDefault().unregister(this);
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
