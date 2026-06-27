package com.example.app.feature.products.domain

import com.example.app.core.common.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProductDetailHandler(
    private val repo: ProductRepository,
    scope: CoroutineScope,
) : BaseHandler<ProductDetailState, ProductsEvent>(ProductDetailState.Loading, scope) {

    fun load(id: Int) = scope.launch {
        setState { ProductDetailState.Loading }
        runCatching { repo.getProduct(id) }
            .onSuccess { setState { ProductDetailState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat detail"
                setState { ProductDetailState.Error(msg) }
                emitEvent(ProductsEvent.ShowError(msg))
            }
    }
}
