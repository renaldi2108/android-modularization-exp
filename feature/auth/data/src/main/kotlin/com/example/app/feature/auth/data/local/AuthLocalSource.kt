package com.example.app.feature.auth.data.local

import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.app.core.common.coroutine.SingletonCoroutineScope
import com.example.app.core.datastore.PreferenceStore
import com.example.app.feature.auth.domain.User
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

interface AuthLocalSource {
    fun observeUser(): Flow<User?>
    fun getUser(): User?
    fun getToken(): String?
    suspend fun saveUser(user: User)
    suspend fun saveToken(token: String)
    suspend fun clearAll()
}

@Singleton
class AuthLocalSourceImpl @Inject constructor(
    store: PreferenceStore,
    moshi: Moshi,
    @SingletonCoroutineScope scope: CoroutineScope
) : AuthLocalSource {
    private val tokenPref by store.preference(stringPreferencesKey("auth_token"), "")
    private val userPref  by store.preference(stringPreferencesKey("auth_user"), "")
    private val userAdapter = moshi.adapter(StoredUser::class.java)

    @Volatile private var cachedToken: String? = null
    private val _user = MutableStateFlow<User?>(null)

    init {
        tokenPref.flow
            .onEach { cachedToken = it.ifBlank { null } }
            .launchIn(scope)
        userPref.flow
            .onEach { json -> _user.value = json.takeIf { it.isNotBlank() }?.let(userAdapter::fromJson)?.toDomain() }
            .launchIn(scope)
    }

    override fun observeUser(): Flow<User?> = _user.asStateFlow()
    override fun getUser(): User?           = _user.value
    override fun getToken(): String?        = cachedToken

    override suspend fun saveUser(user: User) {
        userPref.set(userAdapter.toJson(user.toStored()))
        _user.value = user
    }

    override suspend fun saveToken(token: String) {
        tokenPref.set(token)
        cachedToken = token
    }

    override suspend fun clearAll() {
        tokenPref.delete()
        userPref.delete()
        cachedToken = null
        _user.value = null
    }
}
