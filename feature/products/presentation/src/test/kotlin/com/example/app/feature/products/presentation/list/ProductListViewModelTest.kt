package com.example.app.feature.products.presentation.list

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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {
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

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat data sukses memperbarui state`() = runTest(dispatcher) {
        val vm = ProductListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(1, s.products.size)
        assertEquals(sample, s.products.first())
        assertNull(s.errorMessage)
    }

    @Test
    fun `memuat data gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = ProductListViewModel(FakeRepository(error = IllegalStateException("boom")))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("boom", vm.uiState.value.errorMessage)
    }
    @Test
    fun `QueryChanged memperbarui query`() = runTest(dispatcher) {
        val vm = ProductListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        vm.onAction(ProductListAction.QueryChanged("kopi"))
        advanceUntilIdle()
        assertEquals("kopi", vm.uiState.value.query)
    }

    @Test
    fun `Submit memicu pencarian dengan query terkini`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val vm = ProductListViewModel(repo)
        backgroundScope.launch { vm.uiState.collect {} }
        vm.onAction(ProductListAction.QueryChanged("kopi"))
        vm.onAction(ProductListAction.Submit)
        advanceUntilIdle()
        assertEquals("kopi", repo.searchedQuery)
    }
}
