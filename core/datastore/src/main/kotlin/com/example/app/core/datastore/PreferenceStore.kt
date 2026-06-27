package com.example.app.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty

@Singleton
class PreferenceStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun <T> observe(key: Preferences.Key<T>, default: T): Flow<T> =
        dataStore.data
            .catch { e -> if (e is IOException) emit(emptyPreferences()) else throw e }
            .map { prefs -> prefs[key] ?: default }

    suspend fun <T> get(key: Preferences.Key<T>, default: T): T =
        observe(key, default).first()

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { it.remove(key) }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    fun <T> preference(key: Preferences.Key<T>, default: T): Preference<T> =
        Preference(this, key, default)
}

class Preference<T> internal constructor(
    private val store: PreferenceStore,
    private val key: Preferences.Key<T>,
    private val default: T
) {
    val flow: Flow<T> get() = store.observe(key, default)

    suspend fun get(): T = store.get(key, default)
    suspend fun set(value: T) = store.set(key, value)
    suspend fun delete() = store.remove(key)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Preference<T> = this
}
