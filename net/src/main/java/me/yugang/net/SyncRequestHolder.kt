package me.yugang.net

import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class SyncRequestHolder internal constructor(request: Request) {
    private val httpClient = OkConnection.httpClient

    private val call: Call = httpClient.newCall(request)

    fun executeSync(): Response? {
        return try {
            call.execute()
        } catch (e: IOException) {
            null
        }
    }
}