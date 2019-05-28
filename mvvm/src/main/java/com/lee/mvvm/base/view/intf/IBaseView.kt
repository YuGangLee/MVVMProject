package com.lee.mvvm.base.view.intf

import com.lee.mvvm.utils.LifeCycleHelper

interface IBaseView {
    /**
     * 获取默认的LifeCycleHelper
     *
     * @return [LifeCycleHelper]
     */
    fun getDefaultLifeCycle() : LifeCycleHelper
}
