package com.lee.mvvm.base.constract;

import com.lee.mvvm.utils.LifeCycleHelper;

public interface IBaseView {
    /**
     * 获取默认的LifeCycleHelper
     *
     * @return {@link LifeCycleHelper}
     */
    LifeCycleHelper getDefaultLifeCycle();
}
