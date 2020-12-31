package me.yugang.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.yugang.mvvm.intf.ViewInterface

abstract class MVVMFragment<T : ViewDataBinding> : Fragment(), ViewInterface {
    lateinit var binding: T

    private var isInit = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        onReset(savedInstanceState)
        if (!isInit) {
            loadData(arguments)
            isInit = true
        }
    }

    fun <T : ViewModel> getDefaultViewModel(vmClass: Class<T>): T =
        ViewModelProvider(this).get(vmClass)

    fun <T : ViewModel> getActivityViewModel(vmClass: Class<T>): T =
        ViewModelProvider(requireActivity()).get(vmClass)

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putAll(saveInstanceState() ?: return)
    }

    /**
     * 返回需要保存的数据，将在onSaveInstanceState中调用
     * 保存的数据在Fragment重建时取出，可以通过重写onReset方法获取
     */
    override fun saveInstanceState(): Bundle? {
        return null
    }

    /**
     * 使用Jetpack的Navigation组件进行Fragment管理时,在当前Fragment进入不处于当前导航栈栈顶的时候
     * Fragment对应的View将会被回收,在返回栈顶时将重新创建View;
     * 推荐View层数据通过LiveData进行保持，部分特殊情况可以重写此方法进行数据恢复
     *
     * ps.使用Navigation组件进行Fragment管理时严禁通过持有并复用View的方式进行数据的保存
     *    复用View在某些情况下会导致无法正确的设置Fragment的生命周期
     */
    override fun onReset(savedInstanceState: Bundle?) {}
}