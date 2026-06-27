package com.example.app.core.common.result

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun `safeCall membungkus hasil sukses`() = runTest {
        val result = safeCall { 42 }
        assertTrue(result is Result.Success)
        assertEquals(42, (result as Result.Success).data)
    }

    @Test
    fun `safeCall menangkap exception menjadi Error berisi message dan cause`() = runTest {
        val boom = IllegalStateException("boom")
        val result = safeCall { throw boom }
        assertTrue(result is Result.Error)
        val error = result as Result.Error
        assertEquals("boom", error.message)
        assertSame(boom, error.cause)
    }

    @Test
    fun `safeCall memakai pesan default bila exception tanpa message`() = runTest {
        val result = safeCall { throw RuntimeException() }
        assertEquals("Unknown error", (result as Result.Error).message)
    }
}
