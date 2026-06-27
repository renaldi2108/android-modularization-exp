package com.example.app.core.common.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidatorsTest {

    @Test
    fun `email kosong dianggap valid`() {
        assertNull(Validators.email(""))
    }

    @Test
    fun `email format benar mengembalikan null`() {
        assertNull(Validators.email("eve@example.com"))
    }

    @Test
    fun `email format salah mengembalikan pesan`() {
        assertEquals("Format email tidak valid", Validators.email("bukan-email"))
    }

    @Test
    fun `password kosong dianggap valid`() {
        assertNull(Validators.password(""))
    }

    @Test
    fun `password lebih pendek dari minimum mengembalikan pesan`() {
        assertEquals("Password minimal 8 karakter", Validators.password("123"))
    }

    @Test
    fun `password memenuhi minimum mengembalikan null`() {
        assertNull(Validators.password("rahasia123"))
    }

    @Test
    fun `password menghormati minLength khusus`() {
        assertEquals("Password minimal 4 karakter", Validators.password("ab", minLength = 4))
        assertNull(Validators.password("abcd", minLength = 4))
    }

    @Test
    fun `required kosong mengembalikan pesan dengan label`() {
        assertEquals("Nama wajib diisi", Validators.required("   ", label = "Nama"))
    }

    @Test
    fun `required terisi mengembalikan null`() {
        assertNull(Validators.required("ada"))
    }

    @Test
    fun `allValid true bila tidak ada error dan semua required terisi`() {
        assertTrue(Validators.allValid(errors = listOf(null, null), requiredFilled = listOf("a", "b")))
    }

    @Test
    fun `allValid false bila ada error`() {
        assertFalse(Validators.allValid(errors = listOf(null, "salah"), requiredFilled = listOf("a")))
    }

    @Test
    fun `allValid false bila ada required kosong`() {
        assertFalse(Validators.allValid(errors = listOf(null), requiredFilled = listOf("a", "")))
    }
}
