package com.example.app.core.network.remote

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ApiRequestTest {

    @Test
    fun `get menyetel method GET tanpa body`() {
        val req = ApiRequest.get("users", query = mapOf("page" to "1"))
        assertEquals(HttpMethod.GET, req.method)
        assertEquals("users", req.endpoint)
        assertEquals(mapOf("page" to "1"), req.query)
        assertNull(req.body)
        assertFalse(req.formUrlEncoded)
    }

    @Test
    fun `post menyetel method POST dan body`() {
        val body = mapOf("name" to "eve")
        val req = ApiRequest.post("auth/login", body = body)
        assertEquals(HttpMethod.POST, req.method)
        assertEquals(body, req.body)
    }

    @Test
    fun `put dan patch dan delete menyetel method yang sesuai`() {
        assertEquals(HttpMethod.PUT, ApiRequest.put("users/1", body = "x").method)
        assertEquals(HttpMethod.PATCH, ApiRequest.patch("users/1", body = "x").method)
        assertEquals(HttpMethod.DELETE, ApiRequest.delete("users/1").method)
    }

    @Test
    fun `form default POST dengan formUrlEncoded dan fields`() {
        val req = ApiRequest.form("users", fields = mapOf("page" to "1"))
        assertEquals(HttpMethod.POST, req.method)
        assertTrue(req.formUrlEncoded)
        assertEquals(mapOf("page" to "1"), req.fields)
    }

    @Test
    fun `form bisa memakai method selain POST`() {
        val req = ApiRequest.form("users/1", fields = mapOf("a" to "b"), method = HttpMethod.PUT)
        assertEquals(HttpMethod.PUT, req.method)
        assertTrue(req.formUrlEncoded)
    }

    @Test
    fun `header dan query default kosong`() {
        val req = ApiRequest.get("ping")
        assertTrue(req.headers.isEmpty())
        assertTrue(req.query.isEmpty())
    }
}
