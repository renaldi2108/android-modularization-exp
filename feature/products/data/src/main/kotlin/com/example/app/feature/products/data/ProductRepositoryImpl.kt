package com.example.app.feature.products.data

import com.example.app.core.common.result.Result
import com.example.app.feature.products.data.remote.ProductDto
import com.example.app.feature.products.data.remote.ProductRemoteSource
import com.example.app.feature.products.domain.Product
import com.example.app.feature.products.domain.ProductRepository
import javax.inject.Inject

private fun ProductDto.toDomain() = Product(
    id          = id,
    title       = title,
    description = description,
    price       = price,
    rating      = rating,
    brand       = brand,
    category    = category,
    thumbnail   = thumbnail,
    images      = images,
)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class ProductRepositoryImpl @Inject constructor(
    private val remote: ProductRemoteSource,
) : ProductRepository {

    override suspend fun getProducts(limit: Int): List<Product> =
        remote.getProducts(limit).getOrThrow().products.map { it.toDomain() }

    override suspend fun searchProducts(query: String): List<Product> =
        remote.searchProducts(query).getOrThrow().products.map { it.toDomain() }

    override suspend fun getProduct(id: Int): Product =
        remote.getProduct(id).getOrThrow().toDomain()
}
