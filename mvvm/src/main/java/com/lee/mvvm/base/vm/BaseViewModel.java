package com.lee.mvvm.base.vm;

import com.lee.mvvm.base.constract.IBaseView;
import com.lee.mvvm.base.constract.IBaseViewModel;

public abstract class BaseViewModel implements IBaseViewModel {
    
    protected IBaseView mView;

    @Override
    public void attacheView(IBaseView view) {
        mView = view;
    }

    @Override
    public void viewDetached(IBaseView view) {
        if (mView == null) {
            return;
        }
        if (mView.equals(view)) {
            mView = null;
        }
    }
}
