package com.example.app.feature.carts.presentation.detail

import androidx.lifecycle.SavedStateHandle
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartDetailViewModelTest {
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

    private fun handle() = SavedStateHandle(mapOf("id" to "1"))

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat detail sukses memperbarui state`() = runTest(dispatcher) {
        val vm = CartDetailViewModel(FakeRepository(items = listOf(sample)), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(sample, s.cart)
    }

    @Test
    fun `memuat detail gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = CartDetailViewModel(FakeRepository(error = IllegalStateException("rusak")), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("rusak", vm.uiState.value.errorMessage)
    }
}
