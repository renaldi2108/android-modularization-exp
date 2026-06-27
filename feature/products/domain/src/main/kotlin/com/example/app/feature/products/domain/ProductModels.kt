package com.example.app.feature.products.domain

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val brand: String?,
    val category: String,
    val thumbnail: String,
    val images: List<String>,
)

sealed interface ProductListState {
    data object Loading : ProductListState
    data class Success(val products: List<Product>) : ProductListState
    data class Error(val message: String) : ProductListState
}

sealed interface ProductDetailState {
    data object Loading : ProductDetailState
    data class Success(val product: Product) : ProductDetailState
    data class Error(val message: String) : ProductDetailState
}

sealed interface ProductsEvent {
    data class ShowError(val message: String) : ProductsEvent
}
