package com.example.app.feature.carts.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.app.feature.carts.domain.Cart

@Immutable
data class CartDetailUiState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val errorMessage: String? = null,
)

sealed interface CartDetailAction {
    data object Retry : CartDetailAction
}
