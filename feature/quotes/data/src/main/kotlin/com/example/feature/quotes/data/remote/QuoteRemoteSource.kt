package com.example.feature.quotes.data.remote

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.squareup.moshi.JsonClass
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class QuoteDto(
    val id: Int,
    val quote: String = "",
    val author: String = "",
)

@JsonClass(generateAdapter = true)
data class QuotesResponse(
    val quotes: List<QuoteDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface QuoteRemoteSource {
    suspend fun getQuotes(limit: Int): Result<QuotesResponse>
}

class QuoteRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : QuoteRemoteSource {
    override suspend fun getQuotes(limit: Int): Result<QuotesResponse> =
        remote.fetch(ApiRequest.get("quotes", query = mapOf("limit" to limit.toString())))
}
