package com.example.feature.carts.data

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.example.feature.carts.domain.Cart
import com.example.feature.carts.domain.CartProduct
import com.example.feature.carts.domain.CartRepository
import com.squareup.moshi.JsonClass
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@JsonClass(generateAdapter = true)
data class CartProductDto(
    val id: Int,
    val title: String = "",
    val price: Double = 0.0,
    val quantity: Int = 0,
    val total: Double = 0.0,
)

@JsonClass(generateAdapter = true)
data class CartDto(
    val id: Int,
    val total: Double = 0.0,
    val totalProducts: Int = 0,
    val totalQuantity: Int = 0,
    val userId: Int = 0,
    val products: List<CartProductDto> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class CartsResponse(
    val carts: List<CartDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface CartRemoteSource {
    suspend fun getCarts(limit: Int): Result<CartsResponse>
    suspend fun getCart(id: Int): Result<CartDto>
}

class CartRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : CartRemoteSource {
    override suspend fun getCarts(limit: Int): Result<CartsResponse> =
        remote.fetch(ApiRequest.get("carts", query = mapOf("limit" to limit.toString())))

    override suspend fun getCart(id: Int): Result<CartDto> =
        remote.fetch(ApiRequest.get("carts/$id"))
}

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

@Module
@InstallIn(SingletonComponent::class)
abstract class CartDataModule {
    @Binds @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    abstract fun bindCartRemoteSource(impl: CartRemoteSourceImpl): CartRemoteSource
}
