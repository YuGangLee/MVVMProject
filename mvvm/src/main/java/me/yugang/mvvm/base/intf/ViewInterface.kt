package me.yugang.mvvm.base.intf

import android.os.Bundle
import androidx.annotation.LayoutRes

interface ViewInterface {
    @LayoutRes
    fun layout(): Int

    fun initView()

    fun loadData(data: Bundle?)
}