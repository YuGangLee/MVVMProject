package me.yugang.net

import okhttp3.OkHttpClient
import okhttp3.Request

object OkConnection {
    private var mHttpClient = DefaultClient.httpClient

    val httpClient get() = mHttpClient

    @JvmStatic
    @JvmOverloads
    fun create(url: String = "") = RequestBuilder().url(url)

    @JvmStatic
    fun get(url: String) = RequestBuilder().url(url).method(RequestMethod.GET)

    @JvmStatic
    fun post(url: String) = RequestBuilder().url(url).method(RequestMethod.POST)

    @JvmStatic
    fun setClient(client: OkHttpClient): OkConnection {
        mHttpClient = client
        return this
    }

    @JvmStatic
    fun Request.call() = RequestHolder(this)

    @JvmStatic
    fun Request.syncCall() = SyncRequestHolder(this)
}