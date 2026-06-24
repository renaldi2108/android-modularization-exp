package com.example.feature.quotes.data

import com.example.core.utils.result.Result
import com.example.feature.quotes.data.remote.QuoteDto
import com.example.feature.quotes.data.remote.QuoteRemoteSource
import com.example.feature.quotes.domain.Quote
import com.example.feature.quotes.domain.QuoteRepository
import javax.inject.Inject

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
