package me.yugang.net

import okhttp3.OkHttpClient
import okhttp3.Request

object OkConnection {
    private var mHttpClient = DefaultClient.httpClient

    val httpClient get() = mHttpClient

    /**
     * 生成一个请求地址为[url]的[RequestBuilder]
     * [url]可空
     *
     * @param url 请求地址
     */
    @JvmStatic
    @JvmOverloads
    fun create(url: String = "") = RequestBuilder().url(url)

    /**
     * 生成一个Get请求方式的[RequestBuilder], 请求地址为[url]
     * 请求方式可以通过[RequestBuilder.method]方法重新指定
     *
     * @param url 请求地址
     */
    @JvmStatic
    fun get(url: String) = RequestBuilder().url(url).method(RequestMethod.GET)


    /**
     * 生成一个Post请求方式的[RequestBuilder], 请求地址为[url]
     * 请求方式可以通过[RequestBuilder.method]方法重新指定
     *
     * @param url 请求地址
     */
    @JvmStatic
    fun post(url: String) = RequestBuilder().url(url).method(RequestMethod.POST)

    /**
     * 设置默认使用的OkHttp客户端
     * 对接下来发生的请求生效
     */
    @JvmStatic
    fun setClient(client: OkHttpClient): OkConnection {
        mHttpClient = client
        return this
    }

    /**
     * [Request]的拓展方法, 生成一个异步请求的任务栈
     */
    @JvmStatic
    fun Request.call() = RequestHolder(this)


    /**
     * [Request]的拓展方法, 生成一个同步请求的任务栈
     */
    @JvmStatic
    fun Request.syncCall() = SyncRequestHolder(this)
}