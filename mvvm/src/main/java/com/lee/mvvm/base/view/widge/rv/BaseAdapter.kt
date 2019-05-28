package com.lee.mvvm.base.view.widge.rv

import androidx.databinding.DataBindingUtil
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

/**
 * this adapter is use for DataBinding&RecycleView
 *
 * @param <T> data type
</T> */
open class BaseAdapter<T : Any>
(@param:LayoutRes var layoutId: Int, var variableId: Int, var data: List<T>) :
        RecyclerView.Adapter<BaseViewHolder<*>>(), View.OnClickListener {

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): BaseViewHolder<*> {
        val v = DataBindingUtil
                .inflate<ViewDataBinding>(LayoutInflater.from(viewGroup.context), layoutId, null, false)
                .root
        v.tag = i
        v.setOnClickListener(this)
        return BaseViewHolder<ViewDataBinding>(v)
    }

    override fun onBindViewHolder(baseViewHolder: BaseViewHolder<*>, i: Int) {
        convert(baseViewHolder, data[i])
    }

    override fun onClick(v: View) {
        if (onItemClickListener != null) {
            onItemClickListener!!.onItemClick(v.tag as Int)
        }
    }

    private fun convert(holder: BaseViewHolder<*>, t: T) {
        holder.setBinding(variableId, t)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
