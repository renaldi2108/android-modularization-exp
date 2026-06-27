package com.example.app.feature.products.data

import com.example.app.core.common.result.Result
import com.example.app.feature.products.data.remote.ProductDto
import com.example.app.feature.products.data.remote.ProductsResponse
import com.example.app.feature.products.data.remote.ProductRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductRepositoryImplTest {
    private val dto = ProductDto(id = 1, title = "Item", description = "desc", price = 9.99, rating = 4.5, brand = "Acme", category = "kategori", thumbnail = "thumb", images = emptyList())

    private class FakeRemote(
        var listResult: Result<ProductsResponse> = Result.Success(ProductsResponse()),
        var searchResult: Result<ProductsResponse> = Result.Success(ProductsResponse()),
        var detailResult: Result<ProductDto> = Result.Success(ProductDto(id = 0)),
    ) : ProductRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getProducts(limit: Int) = listResult
        override suspend fun searchProducts(query: String): Result<ProductsResponse> { searchedQuery = query; return searchResult }
        override suspend fun getProduct(id: Int) = detailResult
    }

    @Test
    fun `getProducts sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(ProductsResponse(products = listOf(dto))))
        val repo = ProductRepositoryImpl(remote)
        val result = repo.getProducts()
        assertEquals(1, result.size)
        assertEquals("Item", result[0].title)
    }

    @Test
    fun `getProducts error melempar IllegalStateException berisi message`() = runTest {
        val repo = ProductRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getProducts() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getProducts error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = ProductRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getProducts() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
    @Test
    fun `searchProducts meneruskan query dan memetakan hasil`() = runTest {
        val remote = FakeRemote(searchResult = Result.Success(ProductsResponse(products = listOf(dto))))
        val repo = ProductRepositoryImpl(remote)
        val result = repo.searchProducts("kopi")
        assertEquals("kopi", remote.searchedQuery)
        assertEquals(1, result.size)
    }
    @Test
    fun `getProduct sukses memetakan dto ke domain`() = runTest {
        val repo = ProductRepositoryImpl(FakeRemote(detailResult = Result.Success(dto)))
        val result = repo.getProduct(1)
        assertEquals(1, result.id)
        assertEquals("Item", result.title)
    }

    @Test
    fun `getProduct error melempar IllegalStateException`() = runTest {
        val repo = ProductRepositoryImpl(FakeRemote(detailResult = Result.Error("tidak ditemukan")))
        val thrown = runCatching { repo.getProduct(1) }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("tidak ditemukan", thrown!!.message)
    }
}
