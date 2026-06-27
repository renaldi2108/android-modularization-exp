package com.example.app.feature.products.presentation.list

import androidx.compose.runtime.Immutable
import com.example.app.feature.products.domain.Product
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class ProductListUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val products: ImmutableList<Product> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface ProductListAction {
    data class QueryChanged(val value: String) : ProductListAction
    data object Submit : ProductListAction
    data object Retry : ProductListAction
}
