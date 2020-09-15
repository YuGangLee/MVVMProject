package me.yugang.mvvm.base.intf

import android.os.Bundle
import androidx.annotation.LayoutRes

interface ViewInterface {
    @LayoutRes
    fun layout(): Int

    fun initView()

    fun loadData(data: Bundle?)

    /**
     * View在意外情况下被回收时调用该方法
     */
    fun saveInstanceState(): Bundle?

    /**
     * View重建时调用该方法
     */
    fun onReset(savedInstanceState: Bundle?)
}