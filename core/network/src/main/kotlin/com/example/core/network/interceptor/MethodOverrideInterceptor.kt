package com.example.core.network.interceptor

import com.example.core.network.remote.METHOD_OVERRIDE_HEADER
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MethodOverrideInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val override = request.header(METHOD_OVERRIDE_HEADER)
            ?: return chain.proceed(request)

        val body = if (override in NO_BODY_METHODS) null else request.body

        val rewritten = request.newBuilder()
            .method(override, body)
            .removeHeader(METHOD_OVERRIDE_HEADER)
            .build()
        return chain.proceed(rewritten)
    }

    private companion object {
        val NO_BODY_METHODS = setOf("GET", "HEAD")
    }
}
