package com.lee.mvvm.base.view.widge.rv;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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