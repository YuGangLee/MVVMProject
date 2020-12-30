package me.yugang.net

import okhttp3.Request

object OkConnection {
    var httpClient = DefaultClient.httpClient

    fun newRequest(request: Request) = RequestHolder(request)

    fun newSycnRequest(request: Request) = SyncRequestHolder(request)
}