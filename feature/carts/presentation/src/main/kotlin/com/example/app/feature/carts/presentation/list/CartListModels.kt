package com.example.app.feature.carts.presentation.list

import androidx.compose.runtime.Immutable
import com.example.app.feature.carts.domain.Cart
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CartListUiState(
    val isLoading: Boolean = false,
    val carts: ImmutableList<Cart> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface CartListAction {
    data object Retry : CartListAction
}
