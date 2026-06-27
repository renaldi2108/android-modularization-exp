package com.example.app.core.common.formstate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FormStateTest {

    @Test
    fun `state awal memegang nilai dan belum dirty`() {
        val form = FormState("awal")
        assertEquals("awal", form.value.value)
        assertFalse(form.isDirty.value)
        assertTrue(form.isValid)
    }

    @Test
    fun `update mengubah nilai dan menandai dirty`() {
        val form = FormState("awal")
        form.update { "$it baru" }
        assertEquals("awal baru", form.value.value)
        assertTrue(form.isDirty.value)
    }

    @Test
    fun `setError menambah error dan errorFor mengembalikannya`() {
        val form = FormState("")
        form.setError("email", "salah")
        assertEquals("salah", form.errorFor("email"))
        assertFalse(form.isValid)
    }

    @Test
    fun `setError null menghapus error field`() {
        val form = FormState("")
        form.setError("email", "salah")
        form.setError("email", null)
        assertNull(form.errorFor("email"))
        assertTrue(form.isValid)
    }

    @Test
    fun `clearErrors mengosongkan semua error`() {
        val form = FormState("")
        form.setError("email", "salah")
        form.setError("password", "pendek")
        form.clearErrors()
        assertTrue(form.errors.value.isEmpty())
        assertTrue(form.isValid)
    }

    @Test
    fun `reset mengembalikan nilai, error, dan dirty`() {
        val form = FormState("awal")
        form.update { "berubah" }
        form.setError("email", "salah")
        form.reset("bersih")
        assertEquals("bersih", form.value.value)
        assertTrue(form.errors.value.isEmpty())
        assertFalse(form.isDirty.value)
    }
}
