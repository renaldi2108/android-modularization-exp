package com.example.app.feature.quotes.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuoteHandlersTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Quote(id = 1, quote = "kata", author = "Penulis")

    private class FakeRepository(
        private val items: List<Quote> = emptyList(),
        private val error: Throwable? = null,
    ) : QuoteRepository {
        var searchedQuery: String? = null
        override suspend fun getQuotes(limit: Int): List<Quote> { error?.let { throw it }; return items }
    }

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = QuoteListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(QuoteListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = QuoteListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<QuotesEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is QuoteListState.Error)
        assertEquals("boom", (state as QuoteListState.Error).message)
        assertTrue(events.any { it is QuotesEvent.ShowError })
    }
}
