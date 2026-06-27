package com.example.app.feature.products.domain

import com.example.app.core.common.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ProductListHandler(
    private val repo: ProductRepository,
    scope: CoroutineScope,
) : BaseHandler<ProductListState, ProductsEvent>(ProductListState.Loading, scope) {

    fun load() = scope.launch {
        setState { ProductListState.Loading }
        runCatching { repo.getProducts() }
            .onSuccess { setState { ProductListState.Success(it) } }
            .onFailure { e -> fail(e) }
    }

    fun search(query: String) = scope.launch {
        setState { ProductListState.Loading }
        runCatching { if (query.isBlank()) repo.getProducts() else repo.searchProducts(query) }
            .onSuccess { setState { ProductListState.Success(it) } }
            .onFailure { e -> fail(e) }
    }

    private suspend fun fail(e: Throwable) {
        val msg = e.message ?: "Gagal memuat produk"
        setState { ProductListState.Error(msg) }
        emitEvent(ProductsEvent.ShowError(msg))
    }
}
