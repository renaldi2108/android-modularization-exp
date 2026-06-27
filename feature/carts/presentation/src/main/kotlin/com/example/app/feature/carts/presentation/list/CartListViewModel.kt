package com.example.app.feature.carts.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.carts.domain.CartListHandler
import com.example.app.feature.carts.domain.CartListState
import com.example.app.feature.carts.domain.CartRepository
import com.example.app.feature.carts.domain.CartsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CartListViewModel @Inject constructor(
    repository: CartRepository,
) : BaseViewModel<CartListUiState, CartListAction>(), EventViewModel<CartsEvent> {

    private val handler = CartListHandler(repository, viewModelScope)
    override val events: Flow<CartsEvent> = handler.events

    override fun initialState() = CartListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<CartListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                CartListState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is CartListState.Success -> current.copy(isLoading = false, carts = domain.carts.toImmutableList(), errorMessage = null)
                is CartListState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: CartListAction) = when (action) {
        CartListAction.Retry -> { handler.load(); Unit }
    }
}
