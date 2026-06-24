package com.example.feature.carts.domain

interface CartRepository {
    suspend fun getCarts(limit: Int = 20): List<Cart>
    suspend fun getCart(id: Int): Cart
}
