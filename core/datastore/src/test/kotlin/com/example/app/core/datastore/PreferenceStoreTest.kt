package com.example.app.core.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class PreferenceStoreTest {

    @get:Rule
    val tmp = TemporaryFolder()

    private val dataStoreScope = CoroutineScope(Dispatchers.IO + Job())
    private lateinit var store: PreferenceStore

    private val TOKEN = stringPreferencesKey("token")
    private val COUNT = intPreferencesKey("count")

    @Before
    fun setUp() {
        val dataStore = PreferenceDataStoreFactory.create(scope = dataStoreScope) {
            File(tmp.newFolder(), "test.preferences_pb")
        }
        store = PreferenceStore(dataStore)
    }

    @After
    fun tearDown() {
        dataStoreScope.cancel()
    }

    @Test
    fun `get mengembalikan default saat key belum ada`() = runBlocking {
        assertEquals("none", store.get(TOKEN, "none"))
        assertEquals(0, store.get(COUNT, 0))
    }

    @Test
    fun `set lalu get mengembalikan nilai tersimpan`() = runBlocking {
        store.set(TOKEN, "abc123")
        assertEquals("abc123", store.get(TOKEN, "none"))
    }

    @Test
    fun `observe memancarkan nilai terbaru setelah set`() = runBlocking {
        store.set(COUNT, 7)
        assertEquals(7, store.observe(COUNT, 0).first())
    }

    @Test
    fun `remove mengembalikan ke default`() = runBlocking {
        store.set(TOKEN, "abc123")
        store.remove(TOKEN)
        assertEquals("none", store.get(TOKEN, "none"))
    }

    @Test
    fun `clear menghapus semua key`() = runBlocking {
        store.set(TOKEN, "abc123")
        store.set(COUNT, 7)
        store.clear()
        assertEquals("none", store.get(TOKEN, "none"))
        assertEquals(-1, store.get(COUNT, -1))
    }

    @Test
    fun `preference delegate round-trip`() = runBlocking {
        val pref = store.preference(TOKEN, "none")
        pref.set("xyz")
        assertEquals("xyz", pref.get())
        assertEquals("xyz", pref.flow.first())
    }
}
