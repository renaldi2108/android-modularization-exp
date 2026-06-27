package com.example.app.feature.products.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.products.domain.ProductListHandler
import com.example.app.feature.products.domain.ProductListState
import com.example.app.feature.products.domain.ProductRepository
import com.example.app.feature.products.domain.ProductsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    repository: ProductRepository,
) : BaseViewModel<ProductListUiState, ProductListAction>(), EventViewModel<ProductsEvent> {

    private val handler = ProductListHandler(repository, viewModelScope)
    override val events: Flow<ProductsEvent> = handler.events

    override fun initialState() = ProductListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<ProductListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                ProductListState.Loading       -> current.copy(isLoading = true, errorMessage = null)
                is ProductListState.Success     -> current.copy(
                    isLoading = false,
                    products = domain.products.toImmutableList(),
                    errorMessage = null,
                )
                is ProductListState.Error       -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: ProductListAction) = when (action) {
        is ProductListAction.QueryChanged -> updateUi { copy(query = action.value) }
        ProductListAction.Submit          -> { handler.search(uiState.value.query); Unit }
        ProductListAction.Retry           -> { handler.load(); Unit }
    }
}
