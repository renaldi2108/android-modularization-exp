package com.example.app.feature.auth.data

import com.example.app.core.common.data.BaseRepository
import com.example.app.core.common.result.Result
import com.example.app.feature.auth.data.local.AuthLocalSource
import com.example.app.feature.auth.data.remote.AuthRemoteSource
import com.example.app.feature.auth.data.remote.LoginResponse
import com.example.app.feature.auth.domain.AuthRepository
import com.example.app.feature.auth.domain.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private fun LoginResponse.toDomain() = User(
    id       = id.toString(),
    fullName = "$firstName $lastName".trim(),
    email    = email,
    photoUrl = image,
    token    = accessToken
)

class AuthRepositoryImpl @Inject constructor(
    private val remote: AuthRemoteSource,
    private val local: AuthLocalSource
) : BaseRepository(), AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): User =
        when (val result = remote.login(email, password)) {
            is Result.Success -> result.data.toDomain().also { user ->
                local.saveUser(user)
                local.saveToken(user.token)
            }
            is Result.Error -> throw result.cause ?: IllegalStateException(result.message)
        }

    override suspend fun logout() {
        remote.logout()
        local.clearAll()
    }

    override fun getCurrentUser(): User?    = local.getUser()
    override fun observeUser(): Flow<User?> = local.observeUser()
    override fun getToken(): String?        = local.getToken()
}
