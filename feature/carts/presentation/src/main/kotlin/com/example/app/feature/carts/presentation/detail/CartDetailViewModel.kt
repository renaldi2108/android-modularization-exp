package com.example.app.feature.carts.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.carts.domain.CartDetailHandler
import com.example.app.feature.carts.domain.CartDetailState
import com.example.app.feature.carts.domain.CartRepository
import com.example.app.feature.carts.domain.CartsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CartDetailViewModel @Inject constructor(
    repository: CartRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<CartDetailUiState, CartDetailAction>(), EventViewModel<CartsEvent> {

    private val cartId: Int = checkNotNull(savedStateHandle.get<String>("id")).toInt()
    private val handler = CartDetailHandler(repository, viewModelScope)
    override val events: Flow<CartsEvent> = handler.events

    override fun initialState() = CartDetailUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<CartDetailUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                CartDetailState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is CartDetailState.Success -> current.copy(isLoading = false, cart = domain.cart, errorMessage = null)
                is CartDetailState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load(cartId) }

    override fun onAction(action: CartDetailAction) = when (action) {
        CartDetailAction.Retry -> { handler.load(cartId); Unit }
    }
}
