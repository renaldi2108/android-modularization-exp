package com.example.feature.carts.domain

data class CartProduct(
    val id: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val total: Double,
)

data class Cart(
    val id: Int,
    val total: Double,
    val totalProducts: Int,
    val totalQuantity: Int,
    val userId: Int,
    val products: List<CartProduct>,
)

sealed interface CartListState {
    data object Loading : CartListState
    data class Success(val carts: List<Cart>) : CartListState
    data class Error(val message: String) : CartListState
}

sealed interface CartDetailState {
    data object Loading : CartDetailState
    data class Success(val cart: Cart) : CartDetailState
    data class Error(val message: String) : CartDetailState
}

sealed interface CartsEvent {
    data class ShowError(val message: String) : CartsEvent
}
