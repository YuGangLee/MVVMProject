package me.yugang.net

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLConnection

class RequestBuilder internal constructor() {
    companion object {
        const val JSON_MEDIA_TYPE = "application/json; charset=utf-8"
        const val STREAM_MEDIA_TYPE = "application/octet-stream"
    }

    private var url = ""
    private val headers = HashMap<String, String>()
    private val params = HashMap<String, String>()
    private val bodies = mutableListOf<RequestBody>()
    private val files = mutableListOf<FileParam>()
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

    fun param(paramName: String, param: File): RequestBuilder {
        files.add(FileParam(paramName, param))
        return this
    }

    fun param(paramName: String, param: File, fileName: String): RequestBuilder {
        files.add(FileParam(paramName, param, fileName))
        return this
    }

    fun param(paramName: String, param: List<File>): RequestBuilder {
        param.forEach { file ->
            files.add(FileParam(paramName, file))
        }
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

    fun build(): RequestHolder {
        val builder = Request.Builder()
        headers.forEach { entry -> builder.addHeader(entry.key, entry.value) }
        when (requestMethod) {
            RequestMethod.GET -> {
                if (params.isNotEmpty()) {
                    url = url.plus("?")
                    params.forEach { entry ->
                        url = url.plus("${entry.key}=${entry.value}&")
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
        builder.url(url)
        return RequestHolder(builder.build())
    }

    private fun guessMimeType(fileName: String): MediaType? {
        val fileNameMap = URLConnection.getFileNameMap()
        val name = fileName.replace("#", "")
        val contentType =
            fileNameMap.getContentTypeFor(name) ?: return STREAM_MEDIA_TYPE.toMediaType()
        return contentType.toMediaType()
    }

    private fun createRequestBody(): RequestBody {
        val multipartBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        return if (params.isNotEmpty() && (bodies.isNotEmpty() || files.isNotEmpty())) {
            params.forEach { entry -> multipartBuilder.addFormDataPart(entry.key, entry.value) }
            bodies.forEach { multipartBuilder.addPart(it) }
            files.forEach {
                multipartBuilder.addFormDataPart(
                    it.key,
                    it.name ?: it.file.name,
                    it.file.asRequestBody(guessMimeType(it.file.name))
                )
            }
            multipartBuilder.build()
        } else if (bodies.isNotEmpty()) {
            if (bodies.size == 1) {
                return bodies[0]
            } else {
                bodies.forEach { multipartBuilder.addPart(it) }
                multipartBuilder.build()
            }
        } else {
            val formBuilder = FormBody.Builder()
            params.forEach { entry -> formBuilder.add(entry.key, entry.value) }
            formBuilder.build()
        }
    }

    private data class FileParam(val key: String, val file: File, val name: String?) {
        constructor(key: String, file: File) : this(key, file, null)
    }
}