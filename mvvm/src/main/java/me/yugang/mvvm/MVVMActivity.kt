package me.yugang.mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.yugang.mvvm.intf.ViewInterface

abstract class MVVMActivity<T : ViewDataBinding> : AppCompatActivity(), ViewInterface {
    protected val TAG = this.javaClass.name
    
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this
        initView()
        loadData(intent.extras)
    }

    fun <T : ViewModel> getDefaultViewModel(vmClass: Class<T>): T =
        ViewModelProvider(this).get(vmClass)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putAll(saveInstanceState() ?: return)
    }

    override fun saveInstanceState(): Bundle? {
        return null
    }

    override fun onReset(savedInstanceState: Bundle?) {}
}