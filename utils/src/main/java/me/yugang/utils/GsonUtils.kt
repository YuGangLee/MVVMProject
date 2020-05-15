package me.yugang.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException

object GsonUtils {
    val instance: Gson = GsonBuilder().registerTypeAdapter(
        String::class.java,
        StringNullAdapter()
    ).create()

    internal class StringNullAdapter :
        TypeAdapter<String?>() {
        @Throws(IOException::class)
        override fun write(
            jsonWriter: JsonWriter,
            s: String?
        ) {
            if (s == null) { //序列化使用的是adapter的write方法
                jsonWriter.value("")
                return
            }
            jsonWriter.value(s)
        }

        @Throws(IOException::class)
        override fun read(jsonReader: JsonReader): String? {
            if (jsonReader.peek() == JsonToken.NULL) { //反序列化使用的是read方法
                jsonReader.nextNull()
                return ""
            }
            return jsonReader.nextString()
        }
    }
}