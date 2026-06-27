package com.example.app.feature.carts.domain

import com.example.app.core.common.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CartListHandler(
    private val repo: CartRepository,
    scope: CoroutineScope,
) : BaseHandler<CartListState, CartsEvent>(CartListState.Loading, scope) {

    fun load() = scope.launch {
        setState { CartListState.Loading }
        runCatching { repo.getCarts() }
            .onSuccess { setState { CartListState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat keranjang"
                setState { CartListState.Error(msg) }
                emitEvent(CartsEvent.ShowError(msg))
            }
    }
}

class CartDetailHandler(
    private val repo: CartRepository,
    scope: CoroutineScope,
) : BaseHandler<CartDetailState, CartsEvent>(CartDetailState.Loading, scope) {

    fun load(id: Int) = scope.launch {
        setState { CartDetailState.Loading }
        runCatching { repo.getCart(id) }
            .onSuccess { setState { CartDetailState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat detail"
                setState { CartDetailState.Error(msg) }
                emitEvent(CartsEvent.ShowError(msg))
            }
    }
}
