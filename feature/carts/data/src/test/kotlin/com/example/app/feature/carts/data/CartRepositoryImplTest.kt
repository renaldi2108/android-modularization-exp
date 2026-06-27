package com.example.app.feature.carts.data

import com.example.app.core.common.result.Result
import com.example.app.feature.carts.data.remote.CartDto
import com.example.app.feature.carts.data.remote.CartProductDto
import com.example.app.feature.carts.data.remote.CartsResponse
import com.example.app.feature.carts.data.remote.CartRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CartRepositoryImplTest {
    private val dto = CartDto(id = 1, total = 99.0, totalProducts = 1, totalQuantity = 2, userId = 5, products = listOf(CartProductDto(id = 11, title = "Item", price = 5.0, quantity = 2, total = 10.0)))

    private class FakeRemote(
        var listResult: Result<CartsResponse> = Result.Success(CartsResponse()),
        var detailResult: Result<CartDto> = Result.Success(CartDto(id = 0)),
    ) : CartRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getCarts(limit: Int) = listResult
        override suspend fun getCart(id: Int) = detailResult
    }

    @Test
    fun `getCarts sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(CartsResponse(carts = listOf(dto))))
        val repo = CartRepositoryImpl(remote)
        val result = repo.getCarts()
        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals(1, result[0].products.size)
    }

    @Test
    fun `getCarts error melempar IllegalStateException berisi message`() = runTest {
        val repo = CartRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getCarts() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getCarts error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = CartRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getCarts() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
    @Test
    fun `getCart sukses memetakan dto ke domain`() = runTest {
        val repo = CartRepositoryImpl(FakeRemote(detailResult = Result.Success(dto)))
        val result = repo.getCart(1)
        assertEquals(1, result.id)
        assertEquals(1, result.products.size)
    }

    @Test
    fun `getCart error melempar IllegalStateException`() = runTest {
        val repo = CartRepositoryImpl(FakeRemote(detailResult = Result.Error("tidak ditemukan")))
        val thrown = runCatching { repo.getCart(1) }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("tidak ditemukan", thrown!!.message)
    }
}
