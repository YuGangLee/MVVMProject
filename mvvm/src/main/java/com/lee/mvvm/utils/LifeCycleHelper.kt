package com.lee.mvvm.utils


import com.trello.rxlifecycle3.LifecycleProvider
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LifeCycleHelper(private val lifecycleProvider: LifecycleProvider<*>?) {

    /**
     * 开始进行事件订阅
     *
     * @param observable 被观察者
     * @param observer   观察者
     */
    fun startObserve(observable: Observable<*>?, observer: Observer<Any?>?) {
        if (observable == null || observer == null) {
            return
        }
        if (lifecycleProvider != null) {
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .compose(lifecycleProvider.bindToLifecycle())
                    .subscribe(observer)
        } else {
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(observer)
        }
    }
}
