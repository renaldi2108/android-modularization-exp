package com.example.feature.carts.data

import com.example.core.utils.result.Result
import com.example.feature.carts.data.remote.CartDto
import com.example.feature.carts.data.remote.CartProductDto
import com.example.feature.carts.data.remote.CartRemoteSource
import com.example.feature.carts.domain.Cart
import com.example.feature.carts.domain.CartProduct
import com.example.feature.carts.domain.CartRepository
import javax.inject.Inject

private fun CartProductDto.toDomain() = CartProduct(id, title, price, quantity, total)
private fun CartDto.toDomain() = Cart(
    id = id, total = total, totalProducts = totalProducts, totalQuantity = totalQuantity,
    userId = userId, products = products.map { it.toDomain() },
)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class CartRepositoryImpl @Inject constructor(
    private val remote: CartRemoteSource,
) : CartRepository {
    override suspend fun getCarts(limit: Int) = remote.getCarts(limit).getOrThrow().carts.map { it.toDomain() }
    override suspend fun getCart(id: Int) = remote.getCart(id).getOrThrow().toDomain()
}
