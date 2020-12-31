package me.yugang.mvvm.intf

import android.os.Bundle
import androidx.annotation.LayoutRes

interface ViewInterface {
    val layoutRes: Int

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