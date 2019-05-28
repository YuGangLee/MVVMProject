package com.lee.mvvm.base.view

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.Observer

import com.jeremyliao.liveeventbus.LiveEventBus
import com.lee.mvvm.base.view.intf.IBaseView
import com.lee.mvvm.utils.LifeCycleHelper
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity

abstract class BaseActivity<B : ViewDataBinding, VM : ViewModel> : RxAppCompatActivity(), IBaseView {

    protected lateinit var binding: B
    protected lateinit var vm: VM
    protected var isStartingActivity: Boolean = false
    protected var interceptor: IInterceptor? = null
    protected var isFront: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, bindView())
        vm = initVM()
        preSet()
        initView()
        loadData(intent.extras)
        binding.setLifecycleOwner(this)
    }

    override fun getDefaultLifeCycle() = LifeCycleHelper(this)

    /**
     * add status listener for StartActivity or QuitApp
     */
    protected fun preSet() {
        LiveEventBus.get()
                .with(START_ACTIVITY, StartActivityBundle::class.java)
                .observe(this, Observer<StartActivityBundle> {
                    if (it != null || isFront) {
                        startActivity(it.activityClass, it.data)
                    }
                })
        LiveEventBus.get()
                .with(QUIT_APP)
                .observe(this, Observer { finish() })
    }

    protected fun getDefaultVMInstance(vmClass: Class<VM>): VM {
        return ViewModelProviders.of(this).get(javaClass.name, vmClass)
    }

    protected fun startActivity(activityClass: Class<out Activity>) {
        this.startActivity(activityClass, null)
    }

    protected fun startActivity(activityClass: Class<out Activity>?, data: Bundle?) {
        if (interceptor != null && interceptor!!.process() || isStartingActivity) {
            return
        }
        isStartingActivity = true
        val intent = Intent(this, activityClass)
        if (data != null) {
            intent.putExtras(data)
        }
        runOnUiThread {
            startActivity(intent)
            isStartingActivity = false
        }
    }

    override fun onResume() {
        super.onResume()
        isFront = true
    }

    override fun onPause() {
        isFront = false
        super.onPause()
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

    override fun onDestroy() {
        super.onDestroy()
    }

    class StartActivityBundle {
        var activityClass: Class<BaseActivity<*, *>>? = null
        var data: Bundle? = null
    }

    interface IInterceptor {
        fun init()

        fun process(): Boolean
    }

    companion object {
        val START_ACTIVITY = "com.lee.mvvm.base.view.BaseActivity:START_ACTIVITY"
        val QUIT_APP = "com.lee.mvvm.base.view.BaseActivity:QUIT_APP"
    }
}
