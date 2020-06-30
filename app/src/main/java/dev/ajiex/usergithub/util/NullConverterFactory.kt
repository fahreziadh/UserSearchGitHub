package dev.ajiex.usergithub.util

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class NullConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return object : Converter<ResponseBody, Any> {
            override fun convert(value: ResponseBody): Any? {
                return if (value.contentLength() == 0L) null else delegate.convert(value)
            }

        }
    }
}