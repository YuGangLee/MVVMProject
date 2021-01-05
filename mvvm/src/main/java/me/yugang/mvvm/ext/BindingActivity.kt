package me.yugang.mvvm.ext

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import me.yugang.mvvm.intf.ViewInterface

abstract class BindingActivity<T : ViewBinding> : KeyboardActivity(), ViewInterface {
    val ACTIVITY_TAG = this.javaClass.name

    private lateinit var _binding: T

    val binding get() = _binding

    override val layoutRes: Int
        get() = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindView(layoutInflater)
        setContentView(_binding.root)
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

    abstract fun bindView(inflater: LayoutInflater): T
}