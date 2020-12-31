package me.yugang.mvvm.ext

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.yugang.mvvm.intf.ViewInterface

abstract class BaseActivity : KeyboardActivity(), ViewInterface {
    val ACTIVITY_TAG = this.javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
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