package com.example.app.core.network.interceptor

import com.example.app.core.network.remote.METHOD_OVERRIDE_HEADER
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MethodOverrideInterceptorTest {
    private fun clientCapturing(capture: (Request) -> Unit) = OkHttpClient.Builder()
        .addInterceptor(MethodOverrideInterceptor())
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
    fun `noBody placeholder GET dikirim sebagai DELETE`() {
        var sent: Request? = null
        val client = clientCapturing { sent = it }

        val req = Request.Builder()
            .url("http://localhost/users/1")
            .header(METHOD_OVERRIDE_HEADER, "DELETE")
            .method("GET", null)
            .build()
        client.newCall(req).execute()

        assertEquals("DELETE", sent!!.method)
        assertNull("header override harus dibuang", sent!!.header(METHOD_OVERRIDE_HEADER))
    }

    @Test
    fun `override ke GET membuang body form (tidak melanggar aturan OkHttp)`() {
        var sent: Request? = null
        val client = clientCapturing { sent = it }

        val req = Request.Builder()
            .url("http://localhost/users")
            .header(METHOD_OVERRIDE_HEADER, "GET")
            .method("POST", "page=1".toRequestBody())
            .build()
        client.newCall(req).execute()

        assertEquals("GET", sent!!.method)
        assertNull("GET tidak boleh punya body", sent!!.body)
    }

    @Test
    fun `withBody placeholder POST dikirim sebagai PATCH dengan body utuh`() {
        var sent: Request? = null
        val client = clientCapturing { sent = it }

        val body = "{\"name\":\"x\"}".toRequestBody()
        val req = Request.Builder()
            .url("http://localhost/users/1")
            .header(METHOD_OVERRIDE_HEADER, "PATCH")
            .method("POST", body)
            .build()
        client.newCall(req).execute()

        assertEquals("PATCH", sent!!.method)
        assertEquals(body, sent!!.body)
        assertNull(sent!!.header(METHOD_OVERRIDE_HEADER))
    }
}
