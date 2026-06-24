package com.example.feature.auth.data.remote

import com.example.core.utils.result.Result
import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "id")           val id: Int,
    @Json(name = "username")     val username: String,
    @Json(name = "email")        val email: String,
    @Json(name = "firstName")    val firstName: String,
    @Json(name = "lastName")     val lastName: String,
    @Json(name = "image")        val image: String?,
    @Json(name = "accessToken")  val accessToken: String,
    @Json(name = "refreshToken") val refreshToken: String? = null,
)

interface AuthRemoteSource {
    suspend fun login(credential: String, password: String): Result<LoginResponse>
    suspend fun logout(): Result<Unit>
}

class AuthRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : AuthRemoteSource {
    override suspend fun login(credential: String, password: String): Result<LoginResponse> =
        remote.fetch(ApiRequest.post("auth/login", body = LoginRequest(credential, password)))

    override suspend fun logout(): Result<Unit> = Result.Success(Unit)
}
