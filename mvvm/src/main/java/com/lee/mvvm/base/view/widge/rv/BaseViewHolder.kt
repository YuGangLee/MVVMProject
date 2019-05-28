package com.lee.mvvm.base.view.widge.rv

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import android.view.View

/**
 * this VH is use for DataBinding&RecycleView
 */
class BaseViewHolder<T : ViewDataBinding>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected var binding: T? = DataBindingUtil.bind(itemView)

    fun setBinding(variableId: Int, any: Any): BaseViewHolder<*> {
        binding!!.setVariable(variableId, any)
        binding!!.executePendingBindings()
        return this
    }
}