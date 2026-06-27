package com.example.app.feature.carts.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.app.core.shared.designsystem.theme.AppTheme
import com.example.app.feature.carts.domain.Cart
import com.example.app.feature.carts.domain.CartProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartDetailScreen(
    state: CartDetailUiState,
    onAction: (CartDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.cart?.let { "Keranjang #${it.id}" } ?: "Detail Keranjang", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                state.cart != null -> CartDetailContent(state.cart)
                state.errorMessage != null -> Box(Modifier.fillMaxSize(), Alignment.Center) { Text(state.errorMessage, color = AppTheme.colors.error) }
            }
        }
    }
}

@Composable
private fun CartDetailContent(cart: Cart) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(AppTheme.dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
    ) {
        item {
            Text("Total: $ ${cart.total}", style = AppTheme.typography.titleMedium, color = AppTheme.colors.primary)
            Text("${cart.totalProducts} produk · ${cart.totalQuantity} item", style = AppTheme.typography.bodySmall, color = AppTheme.colors.onSurfaceVariant)
            HorizontalDivider(Modifier.padding(top = AppTheme.dimens.spaceSm))
        }
        items(cart.products, key = { it.id }) { product -> CartProductRow(product) }
    }
}

@Composable
private fun CartProductRow(product: CartProduct) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("${product.quantity}× ${product.title}", style = AppTheme.typography.bodyMedium, modifier = Modifier.padding(end = AppTheme.dimens.spaceSm))
        Text("$ ${product.total}", style = AppTheme.typography.bodyMedium, color = AppTheme.colors.primary)
    }
}
