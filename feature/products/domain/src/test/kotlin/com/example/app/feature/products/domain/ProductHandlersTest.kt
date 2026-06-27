package com.example.app.feature.products.domain

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
class ProductHandlersTest {
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

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = ProductListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(ProductListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = ProductListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<ProductsEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is ProductListState.Error)
        assertEquals("boom", (state as ProductListState.Error).message)
        assertTrue(events.any { it is ProductsEvent.ShowError })
    }
    @Test
    fun `search dengan query memanggil pencarian`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = ProductListHandler(repo, backgroundScope)
        handler.search("kopi")
        advanceUntilIdle()
        assertEquals("kopi", repo.searchedQuery)
        assertEquals(ProductListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `search dengan query kosong memuat semua`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = ProductListHandler(repo, backgroundScope)
        handler.search("   ")
        advanceUntilIdle()
        assertEquals(null, repo.searchedQuery)
        assertEquals(ProductListState.Success(listOf(sample)), handler.state.value)
    }
    @Test
    fun `detail load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = ProductDetailHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        assertEquals(ProductDetailState.Success(sample), handler.state.value)
    }

    @Test
    fun `detail load gagal menghasilkan Error`() = runTest(dispatcher) {
        val handler = ProductDetailHandler(FakeRepository(error = IllegalStateException("rusak")), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is ProductDetailState.Error)
        assertEquals("rusak", (state as ProductDetailState.Error).message)
    }
}
