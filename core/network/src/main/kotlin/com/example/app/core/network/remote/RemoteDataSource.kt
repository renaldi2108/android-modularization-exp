package com.example.app.core.network.remote

import com.example.app.core.common.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Response
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(
    private val service: DynamicApiService,
    private val moshi: Moshi
) {
    suspend fun call(request: ApiRequest): Result<Unit> = try {
        val response = execute(request)
        if (response.isSuccessful) Result.Success(Unit) else response.toError()
    } catch (e: Exception) {
        Result.Error(e.message ?: "Network error", e)
    }

    @PublishedApi
    internal suspend fun requestRaw(request: ApiRequest, type: Type): Result<Any?> = try {
        val response = execute(request)
        if (response.isSuccessful) {
            response.body()?.use { body ->
                Result.Success(moshi.adapter<Any>(type).fromJson(body.source()))
            } ?: Result.Success(null)
        } else {
            response.toError()
        }
    } catch (e: Exception) {
        Result.Error(e.message ?: "Network error", e)
    }

    private suspend fun execute(r: ApiRequest): Response<ResponseBody> =
        service.request(r.method.name, r.endpoint, fieldsOf(r), r.query, r.headers)

    private fun fieldsOf(r: ApiRequest): Map<String, String> {
        if (r.fields.isNotEmpty()) return r.fields
        val body = r.body ?: return emptyMap()

        @Suppress("UNCHECKED_CAST")
        val json = (moshi.adapter(body.javaClass) as JsonAdapter<Any>).toJson(body)
        val map = moshi.adapter(Map::class.java).fromJson(json) ?: emptyMap<Any?, Any?>()
        return map.entries
            .filter { it.value != null }
            .associate { it.key.toString() to it.value.toString() }
    }

    private fun Response<ResponseBody>.toError(): Result.Error {
        val detail = errorBody()?.string()?.takeIf { it.isNotBlank() } ?: message()
        return Result.Error("HTTP ${code()}: $detail")
    }
}

suspend inline fun <reified T : Any> RemoteDataSource.fetch(request: ApiRequest): Result<T> {
    @Suppress("UNCHECKED_CAST")
    return requestRaw(request, T::class.java) as Result<T>
}
