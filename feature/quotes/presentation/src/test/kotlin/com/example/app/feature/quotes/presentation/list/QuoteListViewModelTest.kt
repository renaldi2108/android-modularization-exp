package com.example.app.feature.quotes.presentation.list

import com.example.app.feature.quotes.domain.Quote
import com.example.app.feature.quotes.domain.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuoteListViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Quote(id = 1, quote = "kata", author = "Penulis")

    private class FakeRepository(
        private val items: List<Quote> = emptyList(),
        private val error: Throwable? = null,
    ) : QuoteRepository {
        var searchedQuery: String? = null
        override suspend fun getQuotes(limit: Int): List<Quote> { error?.let { throw it }; return items }
    }

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat data sukses memperbarui state`() = runTest(dispatcher) {
        val vm = QuoteListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(1, s.quotes.size)
        assertEquals(sample, s.quotes.first())
        assertNull(s.errorMessage)
    }

    @Test
    fun `memuat data gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = QuoteListViewModel(FakeRepository(error = IllegalStateException("boom")))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("boom", vm.uiState.value.errorMessage)
    }
}
