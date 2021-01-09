package me.yugang.net

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URLConnection

/**
 * 异步请求的任务栈构建器
 */
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

    /**
     * 设置请求Url
     */
    fun url(url: String): RequestBuilder {
        this.url = url
        return this
    }

    /**
     * 添加请求头
     *
     * @param headerName 请求头的key值
     * @param header 请求头的value值
     */
    fun head(headerName: String, header: String): RequestBuilder {
        if (headers.containsKey(headerName)) {
            headers.remove(headerName)
        }
        headers[headerName] = header
        return this
    }

    /**
     * 添加表单内容
     *
     * 当请求方式[requestMethod]为[RequestMethod.GET]时，参数将会被拼接到请求地址的尾部
     *
     * @param paramName 表单键值
     * @param param 表单数据
     */
    fun param(paramName: String, param: String): RequestBuilder {
        if (params.containsKey(paramName)) {
            params.remove(paramName)
        }
        params[paramName] = param
        return this
    }

    /**
     * 添加上传文件
     *
     * 支持添加多个文件
     * 上传文件名指定为默认的文件名
     * 当请求方式[requestMethod]为[RequestMethod.GET]时该方法无效
     *
     * @see FileParam
     * @param paramName 表单键值
     * @param param 需要上传的文件
     */
    fun param(paramName: String, param: File): RequestBuilder {
        files.add(FileParam(paramName, param))
        return this
    }

    /**
     * 添加上传文件
     *
     * 支持添加多个文件
     * 上传文件名指定为[fileName]
     * 当请求方式[requestMethod]为[RequestMethod.GET]时该方法无效
     *
     * @see FileParam
     * @param paramName 表单键值
     * @param param 需要上传的文件
     * @param fileName 指定使用的文件名
     */
    fun param(paramName: String, param: File, fileName: String): RequestBuilder {
        files.add(FileParam(paramName, param, fileName))
        return this
    }

    /**
     * 批量添加上传文件
     *
     * 上传文件名指定为默认的文件名
     * 当请求方式[requestMethod]为[RequestMethod.GET]时该方法无效
     *
     * @see param(paramName: String, param: File)
     */
    fun param(paramName: String, param: List<File>): RequestBuilder {
        param.forEach { file ->
            files.add(FileParam(paramName, file))
        }
        return this
    }

    /**
     * 使用Json上传数据，将会被打包成一个独立的[RequestBody]
     */
    fun upJson(json: String): RequestBuilder {
        val mediaType = JSON_MEDIA_TYPE.toMediaType()
        bodies.add(json.toRequestBody(mediaType))
        return this
    }

    /**
     * 添加一个请求体
     */
    fun putBody(body: RequestBody): RequestBuilder {
        bodies.add(body)
        return this
    }

    /**
     * 设置请求方式
     */
    fun method(method: RequestMethod): RequestBuilder {
        requestMethod = method
        return this
    }

    /**
     * 构建一个异步的任务栈
     */
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

    /**
     * 构建一个同步的任务栈
     */
    fun buildSync(): SyncRequestHolder {
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
        return SyncRequestHolder(builder.build())
    }

    private fun guessMimeType(fileName: String): MediaType? {
        val fileNameMap = URLConnection.getFileNameMap()
        val name = fileName.replace("#", "")
        val contentType =
            fileNameMap.getContentTypeFor(name) ?: return STREAM_MEDIA_TYPE.toMediaType()
        return contentType.toMediaType()
    }

    /**
     * 创建请求体
     *
     * 默认使用表单形式进行提交
     * 当添加了多个请求体时或者添加了文件时使用混合包模式进行提交
     */
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
        } else if (bodies.isNotEmpty() || files.isNotEmpty()) {
            if (bodies.size == 1 && files.isEmpty()) {
                return bodies[0]
            }
            bodies.forEach { multipartBuilder.addPart(it) }
            files.forEach {
                multipartBuilder.addFormDataPart(
                    it.key,
                    it.name ?: it.file.name,
                    it.file.asRequestBody(guessMimeType(it.file.name))
                )
            }
            multipartBuilder.build()
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