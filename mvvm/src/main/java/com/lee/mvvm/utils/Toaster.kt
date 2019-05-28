package com.lee.mvvm.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.StringRes
import android.widget.Toast

/**
 * 一个简单的Toast工具
 */
object Toaster {

    private val handler = Handler(Looper.getMainLooper())

    private var toast: Toast? = null

    @Synchronized
    fun toastShort(msg: CharSequence) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(AppUtils.context, msg, Toast.LENGTH_SHORT)
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post { toast!!.show() }
        } else {
            toast!!.show()
        }
    }

    @Synchronized
    fun toastLong(msg: CharSequence) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(AppUtils.context, msg, Toast.LENGTH_LONG)
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post { toast!!.show() }
        } else {
            toast!!.show()
        }
    }

    @Synchronized
    fun toastShort(@StringRes stringId: Int) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(AppUtils.context, stringId, Toast.LENGTH_SHORT)
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post { toast!!.show() }
        } else {
            toast!!.show()
        }
    }

    @Synchronized
    fun toastLong(@StringRes stringId: Int) {
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast.makeText(AppUtils.context, stringId, Toast.LENGTH_LONG)
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post { toast!!.show() }
        } else {
            toast!!.show()
        }
    }
}
