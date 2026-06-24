package com.example.feature.products.domain

interface ProductRepository {
    suspend fun getProducts(limit: Int = 20): List<Product>
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProduct(id: Int): Product
}
