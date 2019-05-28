package com.lee.mvvm.base.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.lee.mvvm.base.view.intf.IBaseView
import com.lee.mvvm.utils.LifeCycleHelper
import com.trello.rxlifecycle3.components.support.RxFragment

abstract class BaseFragment<B : ViewDataBinding, VM : ViewModel> : RxFragment(), IBaseView {

    private var baseView: View? = null
    protected lateinit var binding: B
    protected lateinit var vm: VM

    override fun getDefaultLifeCycle() = LifeCycleHelper(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (baseView == null) {
            binding = DataBindingUtil.inflate(inflater, bindView(), container, false)
            baseView = binding.root
            initView()
            vm = initVM()
            loadData(arguments)
            binding.setLifecycleOwner(this)
        } else if (container != null) {
            val viewGroup = container.parent as ViewGroup
            viewGroup.removeView(this.baseView)
        }
        return baseView
    }

    protected fun getDefaultVMInstance(vmClass: Class<VM>): VM {
        return if (activity != null) {
            ViewModelProviders.of(activity!!).get(activity!!.javaClass.name, vmClass)
        } else ViewModelProviders.of(this).get(javaClass.name, vmClass)
    }

    /**
     * 获取到databinding将要绑定的布局Id
     *
     * @return layoutId
     */
    @LayoutRes
    protected abstract fun bindView(): Int

    /**
     * 进行可能的View初始化
     */
    protected abstract fun initView()

    /**
     * 每一个View默认持有一个ViewModel
     *
     * @return VM: Nullable
     */
    protected abstract fun initVM(): VM

    /**
     * 进行数据的初始化以及Databinding中的数据绑定
     *
     * @param dataFromIntent 从Intent中取到的数据
     */
    protected abstract fun loadData(dataFromIntent: Bundle?)
}
