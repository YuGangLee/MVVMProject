package com.lee.mvvm.base.constract;

import com.lee.mvvm.base.view.BaseActivity;
import com.lee.mvvm.base.view.BaseFragment;

public interface IBaseViewModel {

    /**
     * 通过{@link BaseActivity#initVM()} & {@link BaseFragment#initVM()}中
     * 返回的ViewModel将被默认绑定至对应的Activity或Fragment
     */
    void attacheView();

    void onViewStop();

    /**
     * BaseActivity中默认持有的VM将在activity被销毁时自动解除绑定
     */
    void viewDetached();
}
