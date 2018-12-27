package com.lee.mvvm.base.view_model;

import android.arch.lifecycle.ViewModel;

import com.lee.mvvm.base.view.BaseActivity;
import com.lee.mvvm.base.view.BaseFragment;

public abstract class BaseViewModel extends ViewModel {

    /**
     * 通过{@link BaseActivity#initVM()} & {@link BaseFragment#initVM()}中
     * 返回的ViewModel将被默认绑定至对应的Activity或Fragment
     */
    public abstract void attacheView();

    public abstract void onViewStop();

    /**
     * BaseActivity中默认持有的VM将在activity被销毁时自动解除绑定
     */
    public abstract void viewDetached();
}
