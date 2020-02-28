package me.yugang.net

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception

class RequestHolder private constructor(request: Request) {

    companion object {
        /**
         * 支持自定义HttpClient
         */
        var httpClient = DefaultClient.httpClient

        fun newRequest(request: Request): RequestHolder {
            return RequestHolder(request)
        }
    }

    private var call: Call
    private var onFailure: (exception: Exception) -> Unit = {}
    private var onResponse: (response: Response) -> Unit = {}

    init {
        call = httpClient.newCall(request)
    }

    fun onCallback(
        onResponse: (response: Response) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ): RequestHolder {
        this.onResponse = onResponse
        this.onFailure = onFailure
        return this
    }

    fun onResponse(callback: (response: Response) -> Unit): RequestHolder {
        onResponse = callback
        return this
    }

    fun onFailure(callback: (exception: Exception) -> Unit): RequestHolder {
        onFailure = callback
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

    fun getCall(): Call = call

    private inner class RequestCallback : Callback {
        override fun onFailure(call: Call, e: IOException) {
            onFailure(e)
        }

        override fun onResponse(call: Call, response: Response) {
            try {
                onResponse(response)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}