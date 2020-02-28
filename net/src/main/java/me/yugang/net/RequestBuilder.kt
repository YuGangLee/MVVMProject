package me.yugang.net

import okhttp3.FormBody
import okhttp3.Request
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class RequestBuilder {
    companion object {
        const val JSON_MEDIA_TYPE = "application/json; charset=utf-8"
    }

    private var url = ""
    private val headers = HashMap<String, String>()
    private val params = HashMap<String, String>()
    private val bodies = mutableListOf<RequestBody>()
    private var requestMethod = RequestMethod.GET

    fun url(url: String): RequestBuilder {
        this.url = url
        return this
    }

    fun head(headerName: String, header: String): RequestBuilder {
        if (headers.containsKey(headerName)) {
            headers.remove(headerName)
        }
        headers[headerName] = header
        return this
    }

    fun param(paramName: String, param: String): RequestBuilder {
        if (params.containsKey(paramName)) {
            params.remove(paramName)
        }
        params[paramName] = param
        return this
    }

    fun upJson(json: String): RequestBuilder {
        val mediaType = JSON_MEDIA_TYPE.toMediaType()
        bodies.add(json.toRequestBody(mediaType))
        return this
    }

    fun putBody(body: RequestBody): RequestBuilder {
        bodies.add(body)
        return this
    }

    fun method(method: RequestMethod): RequestBuilder {
        requestMethod = method
        return this
    }

    fun build(): Request {
        val builder = Request.Builder()
        builder.url(url)
        headers.forEach { entry -> builder.addHeader(entry.key, entry.value) }
        when (requestMethod) {
            RequestMethod.GET -> {
                if (params.isNotEmpty()) {
                    url.plus("?")
                    params.forEach { entry ->
                        url.plus("${entry.key}=${entry.value}&")
                    }
                    url = url.substring(0, url.length - 1)
                }
            }
            RequestMethod.POST -> {
                builder.post(createRequestBody())
            }
            RequestMethod.PUT -> {
                builder.put(createRequestBody())
            }
            RequestMethod.DELETE -> {
                builder.delete(createRequestBody())
            }
        }
        return builder.build()
    }

    private fun createRequestBody(): RequestBody {
        val multipartBuilder = MultipartBody.Builder()
        val formBuilder = FormBody.Builder()
        params.forEach { entry -> formBuilder.add(entry.key, entry.value) }
        return if (params.isNotEmpty() && bodies.isNotEmpty()) {
            multipartBuilder.addPart(formBuilder.build())
            bodies.forEach { multipartBuilder.addPart(it) }
            multipartBuilder.build()
        } else if (bodies.isNotEmpty()) {
            bodies.forEach { multipartBuilder.addPart(it) }
            multipartBuilder.build()
        } else {
            formBuilder.build()
        }
    }

    enum class RequestMethod {
        GET,
        POST,
        PUT,
        DELETE
    }
}