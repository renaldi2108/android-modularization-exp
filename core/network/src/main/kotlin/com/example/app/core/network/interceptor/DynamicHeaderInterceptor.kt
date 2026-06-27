package com.example.app.core.network.interceptor

import com.example.app.core.network.NetworkConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class DynamicHeaderInterceptor @Inject constructor(
    private val config: NetworkConfig
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.newBuilder()

        config.dynamicHeaders().forEach { (name, value) ->
            if (original.header(name) == null) builder.header(name, value)
        }

        return chain.proceed(builder.build())
    }
}
