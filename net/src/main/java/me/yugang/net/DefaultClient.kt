package me.yugang.net

import android.util.Log
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object DefaultClient {
    private const val CONNECT_TIMEOUT_SEC = 20L
    private const val READ_TIMEOUT_SEC = 20L
    private const val WRITE_TIMEOUT_SEC = 20L
    private const val MAX_IDLE_CONNECTIONS = 5
    private const val CONNECTION_KEEP_ALIVE_SEC = 5L

    var showLog = BuildConfig.DEBUG

    val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
        .connectionPool(
            ConnectionPool(
                MAX_IDLE_CONNECTIONS,
                CONNECTION_KEEP_ALIVE_SEC,
                TimeUnit.SECONDS
            )
        )
        .cookieJar(MemoryCookieJar())
        .also {
            if (showLog) {
                it.addInterceptor(HttpLoggingInterceptor(HttpLogger())
                    .apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
            }
        }
        .build()

    private class HttpLogger : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            Log.i("okhttp", message)
        }
    }

    private class MemoryCookieJar : CookieJar {
        private val cookieLists = mutableMapOf<String, List<Cookie>>()

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieLists[url.toString()] ?: listOf()
        }

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            if (cookieLists.containsKey(url.toString())) {
                cookieLists.remove(url.toString())
            }
            cookieLists[url.toString()] = cookies
        }
    }
}