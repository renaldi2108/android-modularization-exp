package com.example.feature.products.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.feature.products.domain.Product

@Immutable
data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val errorMessage: String? = null,
)

sealed interface ProductDetailAction {
    data object Retry : ProductDetailAction
}
