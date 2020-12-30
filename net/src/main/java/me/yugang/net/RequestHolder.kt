package me.yugang.net

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class RequestHolder internal constructor(request: Request) : LifecycleEventObserver {
    private val httpClient = OkConnection.httpClient

    private val call: Call = httpClient.newCall(request)

    private var onFailure: (exception: Throwable) -> Unit = {}
    private var onResponse: (response: Response) -> Unit = {}
    private var onFinish: () -> Unit = {}
    private var mCancelOnEvent = Lifecycle.Event.ON_DESTROY
    private var mLifecycleOwner: LifecycleOwner? = null

    @JvmOverloads
    fun with(
        lifecycleOwner: LifecycleOwner,
        cancelOnEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ) {
        lifecycleOwner.lifecycle.addObserver(this)
        mLifecycleOwner = lifecycleOwner
        mCancelOnEvent = cancelOnEvent
    }

    fun onResponse(callback: (response: Response) -> Unit): RequestHolder {
        onResponse = callback
        return this
    }

    fun onFailure(callback: (exception: Throwable) -> Unit): RequestHolder {
        onFailure = callback
        return this
    }

    fun onFinish(callback: () -> Unit): RequestHolder {
        onFinish = callback
        return this
    }

    fun execute(): Call {
        call.enqueue(RequestCallback())
        return call
    }

    private inner class RequestCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e)
            onFinish()
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                onResponse(response)
            } catch (e: Exception) {
                onFailure(e)
            } finally {
                onFinish()
                mLifecycleOwner?.lifecycle?.removeObserver(this@RequestHolder)
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == mCancelOnEvent && !call.isCanceled()) {
            call.cancel()
        }
        if (event == Lifecycle.Event.ON_DESTROY) {
            mLifecycleOwner?.lifecycle?.removeObserver(this)
            mLifecycleOwner = null
        }
    }
}