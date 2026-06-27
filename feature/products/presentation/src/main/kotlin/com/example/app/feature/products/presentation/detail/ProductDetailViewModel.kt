package com.example.app.feature.products.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.products.domain.ProductDetailHandler
import com.example.app.feature.products.domain.ProductDetailState
import com.example.app.feature.products.domain.ProductRepository
import com.example.app.feature.products.domain.ProductsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    repository: ProductRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ProductDetailUiState, ProductDetailAction>(), EventViewModel<ProductsEvent> {

    private val productId: Int = checkNotNull(savedStateHandle.get<String>("id")).toInt()
    private val handler = ProductDetailHandler(repository, viewModelScope)
    override val events: Flow<ProductsEvent> = handler.events

    override fun initialState() = ProductDetailUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<ProductDetailUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                ProductDetailState.Loading      -> current.copy(isLoading = true, errorMessage = null)
                is ProductDetailState.Success    -> current.copy(isLoading = false, product = domain.product, errorMessage = null)
                is ProductDetailState.Error      -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load(productId) }

    override fun onAction(action: ProductDetailAction) = when (action) {
        ProductDetailAction.Retry -> { handler.load(productId); Unit }
    }
}
