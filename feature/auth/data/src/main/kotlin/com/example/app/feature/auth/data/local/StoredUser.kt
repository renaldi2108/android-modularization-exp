package com.example.app.feature.auth.data.local

import com.example.app.feature.auth.domain.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoredUser(
    val id: String,
    val fullName: String,
    val email: String,
    val photoUrl: String?,
    val token: String,
)

fun User.toStored() = StoredUser(id, fullName, email, photoUrl, token)
fun StoredUser.toDomain() = User(id, fullName, email, photoUrl, token)
