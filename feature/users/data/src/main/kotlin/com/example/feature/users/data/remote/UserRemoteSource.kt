package com.example.feature.users.data.remote

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.squareup.moshi.JsonClass
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val image: String = "",
    val age: Int = 0,
    val university: String = "",
)

@JsonClass(generateAdapter = true)
data class UsersResponse(
    val users: List<UserDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface UserRemoteSource {
    suspend fun getUsers(limit: Int): Result<UsersResponse>
    suspend fun searchUsers(query: String): Result<UsersResponse>
    suspend fun getUser(id: Int): Result<UserDto>
}

class UserRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : UserRemoteSource {

    override suspend fun getUsers(limit: Int): Result<UsersResponse> =
        remote.fetch(ApiRequest.get("users", query = mapOf("limit" to limit.toString())))

    override suspend fun searchUsers(query: String): Result<UsersResponse> =
        remote.fetch(ApiRequest.get("users/search", query = mapOf("q" to query)))

    override suspend fun getUser(id: Int): Result<UserDto> =
        remote.fetch(ApiRequest.get("users/$id"))
}
