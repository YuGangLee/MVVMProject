package me.yugang.net

import okhttp3.*
import java.io.IOException
import java.lang.Exception

class RequestHolder internal constructor(request: Request) {
    private val httpClient = OkConnection.httpClient

    private val call: Call = httpClient.newCall(request)

    private var onFailure: (exception: Throwable) -> Unit = {}
    private var onResponse: (response: Response) -> Unit = {}
    private var onFinish: () -> Unit = {}

    fun onCallback(
        onResponse: (response: Response) -> Unit,
        onFailure: (exception: Throwable) -> Unit,
        onFinish: () -> Unit
    ): RequestHolder {
        this.onResponse = onResponse
        this.onFailure = onFailure
        this.onFinish = onFinish
        return this
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

    fun executeAsync(): Call {
        call.enqueue(RequestCallback())
        return call
    }

    fun executeSync(): Response? {
        return try {
            call.execute()
        } catch (e: IOException) {
            null
        }
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
            }
        }
    }
}