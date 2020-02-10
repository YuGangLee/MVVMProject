package me.yugang.mvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import me.yugang.mvvm.base.intf.ViewInterface

abstract class MVVMActivity<T : ViewDataBinding> : AppCompatActivity(), ViewInterface {
    lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layout())
        initView()
        loadData(intent.extras)
    }
}