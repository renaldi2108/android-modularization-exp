package com.example.core.utils.result

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Result<Nothing>()
}

suspend fun <T> safeCall(block: suspend () -> T): Result<T> =
    runCatching { block() }.fold(
        onSuccess = { Result.Success(it) },
        onFailure = { Result.Error(it.message ?: "Unknown error", it) }
    )
