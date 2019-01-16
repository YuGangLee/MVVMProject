package com.lee.mvvm.base.view.widge.rv;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * this VH is use for DataBinding&RecycleView
 */
public class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected T binding;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public BaseViewHolder setBinding(int variableId, Object object) {
        binding.setVariable(variableId, object);
        binding.executePendingBindings();
        return this;
    }
}