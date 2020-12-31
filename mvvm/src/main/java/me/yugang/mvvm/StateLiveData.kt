package me.yugang.mvvm

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData

class StateLiveData<T> @JvmOverloads constructor(value: T? = null) : LiveData<T>(value) {
    public override fun postValue(value: T?) {
        mMainThreadHandle.post {
            setValue(value)
        }
    }

    public override fun setValue(value: T?) {
        super.setValue(value)
    }

    companion object {
        private val mMainThreadHandle = Handler(Looper.getMainLooper())
    }
}