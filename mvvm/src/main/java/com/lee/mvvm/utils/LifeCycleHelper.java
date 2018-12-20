package com.lee.mvvm.utils;

import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LifeCycleHelper {

    private LifecycleProvider lifecycleProvider;

    public LifeCycleHelper(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    /**
     * 开始进行事件订阅
     *
     * @param observable 被观察者
     * @param observer   观察者
     */
    public void startObserve(Observable observable, Observer observer) {
        if (observable == null || observer == null) {
            return;
        }
        if (lifecycleProvider != null) {
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .compose(lifecycleProvider.bindToLifecycle())
                    .subscribe(observer);
        } else {
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(observer);
        }
    }
}
