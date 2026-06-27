package com.example.app.feature.carts.presentation.list

import com.example.app.feature.carts.domain.Cart
import com.example.app.feature.carts.domain.CartRepository
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
class CartListViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Cart(id = 1, total = 99.0, totalProducts = 1, totalQuantity = 2, userId = 5, products = emptyList())

    private class FakeRepository(
        private val items: List<Cart> = emptyList(),
        private val error: Throwable? = null,
    ) : CartRepository {
        var searchedQuery: String? = null
        override suspend fun getCarts(limit: Int): List<Cart> { error?.let { throw it }; return items }
        override suspend fun getCart(id: Int): Cart { error?.let { throw it }; return items.first() }
    }

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat data sukses memperbarui state`() = runTest(dispatcher) {
        val vm = CartListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(1, s.carts.size)
        assertEquals(sample, s.carts.first())
        assertNull(s.errorMessage)
    }

    @Test
    fun `memuat data gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = CartListViewModel(FakeRepository(error = IllegalStateException("boom")))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("boom", vm.uiState.value.errorMessage)
    }
}
