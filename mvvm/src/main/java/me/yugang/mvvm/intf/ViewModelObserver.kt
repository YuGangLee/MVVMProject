package me.yugang.mvvm.intf

import androidx.lifecycle.LifecycleOwner

interface ViewModelObserver : LifecycleOwner {
    fun onLoading()

    fun finishLoad()

    fun onMessage(message: String)

    fun onError(error: Throwable)
}