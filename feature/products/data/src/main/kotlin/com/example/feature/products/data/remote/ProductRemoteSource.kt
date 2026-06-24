package com.example.feature.products.data.remote

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.squareup.moshi.JsonClass
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class ProductDto(
    val id: Int,
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val rating: Double = 0.0,
    val brand: String? = null,
    val category: String = "",
    val thumbnail: String = "",
    val images: List<String> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class ProductsResponse(
    val products: List<ProductDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface ProductRemoteSource {
    suspend fun getProducts(limit: Int): Result<ProductsResponse>
    suspend fun searchProducts(query: String): Result<ProductsResponse>
    suspend fun getProduct(id: Int): Result<ProductDto>
}

class ProductRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : ProductRemoteSource {

    override suspend fun getProducts(limit: Int): Result<ProductsResponse> =
        remote.fetch(ApiRequest.get("products", query = mapOf("limit" to limit.toString())))

    override suspend fun searchProducts(query: String): Result<ProductsResponse> =
        remote.fetch(ApiRequest.get("products/search", query = mapOf("q" to query)))

    override suspend fun getProduct(id: Int): Result<ProductDto> =
        remote.fetch(ApiRequest.get("products/$id"))
}
