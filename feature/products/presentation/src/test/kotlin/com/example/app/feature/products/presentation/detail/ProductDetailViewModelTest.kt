package com.example.app.feature.products.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.app.feature.products.domain.Product
import com.example.app.feature.products.domain.ProductRepository
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
class ProductDetailViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Product(id = 1, title = "Item", description = "desc", price = 9.99, rating = 4.5, brand = "Acme", category = "kategori", thumbnail = "thumb", images = emptyList())

    private class FakeRepository(
        private val items: List<Product> = emptyList(),
        private val error: Throwable? = null,
    ) : ProductRepository {
        var searchedQuery: String? = null
        override suspend fun getProducts(limit: Int): List<Product> { error?.let { throw it }; return items }
        override suspend fun searchProducts(query: String): List<Product> { searchedQuery = query; error?.let { throw it }; return items }
        override suspend fun getProduct(id: Int): Product { error?.let { throw it }; return items.first() }
    }

    private fun handle() = SavedStateHandle(mapOf("id" to "1"))

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat detail sukses memperbarui state`() = runTest(dispatcher) {
        val vm = ProductDetailViewModel(FakeRepository(items = listOf(sample)), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(sample, s.product)
    }

    @Test
    fun `memuat detail gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = ProductDetailViewModel(FakeRepository(error = IllegalStateException("rusak")), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("rusak", vm.uiState.value.errorMessage)
    }
}
