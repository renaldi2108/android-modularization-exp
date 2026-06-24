package com.example.core.network.interceptor

import com.example.core.network.NetworkConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DynamicHeaderInterceptorTest {

    private fun configWith(headers: () -> Map<String, String>) =
        NetworkConfig(baseUrl = "http://localhost/", isDebug = false, dynamicHeaders = headers)

    private fun clientCapturing(config: NetworkConfig, capture: (Request) -> Unit) =
        OkHttpClient.Builder()
            .addInterceptor(DynamicHeaderInterceptor(config))
            .addInterceptor(Interceptor { chain ->
                capture(chain.request())
                Response.Builder()
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .code(200).message("OK")
                    .body("".toResponseBody(null))
                    .build()
            })
            .build()

    @Test
    fun `header dinamis ditambahkan ke request`() {
        var sent: Request? = null
        val client = clientCapturing(
            configWith { mapOf("Authorization" to "Bearer token-123") }
        ) { sent = it }

        client.newCall(Request.Builder().url("http://localhost/users").build()).execute()

        assertEquals("Bearer token-123", sent!!.header("Authorization"))
    }

    @Test
    fun `header per-request tidak ditimpa oleh provider`() {
        var sent: Request? = null
        val client = clientCapturing(
            configWith { mapOf("Authorization" to "Bearer from-provider") }
        ) { sent = it }

        val req = Request.Builder()
            .url("http://localhost/users")
            .header("Authorization", "Bearer per-request")
            .build()
        client.newCall(req).execute()

        assertEquals("Bearer per-request", sent!!.header("Authorization"))
    }

    @Test
    fun `map kosong tidak menambah header apa pun`() {
        var sent: Request? = null
        val client = clientCapturing(configWith { emptyMap() }) { sent = it }

        client.newCall(Request.Builder().url("http://localhost/users").build()).execute()

        assertNull(sent!!.header("Authorization"))
    }
}
