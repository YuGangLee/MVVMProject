package me.yugang.mvvm

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import me.yugang.mvvm.intf.ViewModelObserver

open class BaseViewModel : ViewModel() {
    private val mIsLoading = StateLiveData<Boolean>()
    private val mMsg = StateLiveData<String>()
    private val mError = StateLiveData<Throwable>()

    open fun postLoading() {
        mIsLoading.postValue(true)
    }

    open fun postLoadFinish() {
        mIsLoading.postValue(false)
    }

    open fun postMessage(msg: String?) {
        mMsg.postValue(msg)
    }

    open fun postError(error: Throwable?) {
        mError.postValue(error)
    }

    fun observe(observer: ViewModelObserver) {
        mIsLoading.observe(observer, Observer {
            if (it == true) {
                observer.onLoading()
            } else {
                observer.finishLoad()
            }
        })
        mMsg.observe(observer, Observer {
            observer.onMessage(it ?: "Null Message")
        })
        mError.observe(observer, Observer {
            observer.onError(it ?: UndefinedError())
        })
    }

    class UndefinedError : Throwable() {
        override val message: String
            get() = "Error(Null)"
    }
}