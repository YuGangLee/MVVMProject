package com.lee.mvvm.base.view.widge.rv

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class EmptyRecyclerView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private var mEmptyView: View? = null

    private val mObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val adapter = adapter
            if (adapter == null || mEmptyView == null) {
                return
            }
            if (adapter.itemCount == 0) {
                mEmptyView!!.visibility = View.VISIBLE
                this@EmptyRecyclerView.visibility = View.GONE
            } else {
                mEmptyView!!.visibility = View.GONE
                this@EmptyRecyclerView.visibility = View.VISIBLE
            }
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onChanged()
        }
    }

    fun setEmptyView(view: View) {
        this.mEmptyView = view
        (this.parent as ViewGroup).addView(mEmptyView, (this.parent as ViewGroup).indexOfChild(this))
    }

    fun setEmptyView(@LayoutRes layoutId: Int) {
        val inflater = LayoutInflater.from(context)
        this.mEmptyView = inflater.inflate(layoutId, parent as ViewGroup, false)
        (this.parent as ViewGroup).addView(mEmptyView, (this.parent as ViewGroup).indexOfChild(this))
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        super.setAdapter(adapter)
        adapter!!.registerAdapterDataObserver(mObserver)
        mObserver.onChanged()
    }
}