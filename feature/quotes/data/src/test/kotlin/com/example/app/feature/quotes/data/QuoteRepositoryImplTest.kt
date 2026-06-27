package com.example.app.feature.quotes.data

import com.example.app.core.common.result.Result
import com.example.app.feature.quotes.data.remote.QuoteDto
import com.example.app.feature.quotes.data.remote.QuotesResponse
import com.example.app.feature.quotes.data.remote.QuoteRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuoteRepositoryImplTest {
    private val dto = QuoteDto(id = 1, quote = "kata", author = "Penulis")

    private class FakeRemote(
        var listResult: Result<QuotesResponse> = Result.Success(QuotesResponse()),
    ) : QuoteRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getQuotes(limit: Int) = listResult
    }

    @Test
    fun `getQuotes sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(QuotesResponse(quotes = listOf(dto))))
        val repo = QuoteRepositoryImpl(remote)
        val result = repo.getQuotes()
        assertEquals(1, result.size)
        assertEquals("Penulis", result[0].author)
    }

    @Test
    fun `getQuotes error melempar IllegalStateException berisi message`() = runTest {
        val repo = QuoteRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getQuotes() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getQuotes error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = QuoteRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getQuotes() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
}
