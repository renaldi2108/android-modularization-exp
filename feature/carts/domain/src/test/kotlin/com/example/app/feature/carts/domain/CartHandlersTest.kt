package com.example.app.feature.carts.domain

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
class CartHandlersTest {
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

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = CartListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(CartListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = CartListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<CartsEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is CartListState.Error)
        assertEquals("boom", (state as CartListState.Error).message)
        assertTrue(events.any { it is CartsEvent.ShowError })
    }
    @Test
    fun `detail load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = CartDetailHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        assertEquals(CartDetailState.Success(sample), handler.state.value)
    }

    @Test
    fun `detail load gagal menghasilkan Error`() = runTest(dispatcher) {
        val handler = CartDetailHandler(FakeRepository(error = IllegalStateException("rusak")), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is CartDetailState.Error)
        assertEquals("rusak", (state as CartDetailState.Error).message)
    }
}
