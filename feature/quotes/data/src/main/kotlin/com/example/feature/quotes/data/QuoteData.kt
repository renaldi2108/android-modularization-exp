package com.example.feature.quotes.data

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.example.feature.quotes.domain.Quote
import com.example.feature.quotes.domain.QuoteRepository
import com.squareup.moshi.JsonClass
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

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

private fun QuoteDto.toDomain() = Quote(id = id, quote = quote, author = author)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class QuoteRepositoryImpl @Inject constructor(
    private val remote: QuoteRemoteSource,
) : QuoteRepository {
    override suspend fun getQuotes(limit: Int) = remote.getQuotes(limit).getOrThrow().quotes.map { it.toDomain() }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class QuoteDataModule {
    @Binds @Singleton
    abstract fun bindQuoteRepository(impl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    abstract fun bindQuoteRemoteSource(impl: QuoteRemoteSourceImpl): QuoteRemoteSource
}
