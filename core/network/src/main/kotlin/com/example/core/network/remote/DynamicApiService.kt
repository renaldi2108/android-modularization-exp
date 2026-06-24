package com.example.core.network.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

const val METHOD_OVERRIDE_HEADER = "X-Http-Method"

interface DynamicApiService {
    @FormUrlEncoded
    @POST
    suspend fun request(
        @Header(METHOD_OVERRIDE_HEADER) method: String,
        @Url url: String,
        @FieldMap fields: Map<String, String> = emptyMap(),
        @QueryMap query: Map<String, String> = emptyMap(),
        @HeaderMap headers: Map<String, String> = emptyMap()
    ): Response<ResponseBody>
}
