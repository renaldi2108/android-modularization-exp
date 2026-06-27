package com.example.app.feature.carts.data.remote

import com.example.app.core.network.remote.ApiRequest
import com.example.app.core.network.remote.RemoteDataSource
import com.example.app.core.network.remote.fetch
import com.example.app.core.common.result.Result
import com.squareup.moshi.JsonClass
import javax.inject.Inject

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
