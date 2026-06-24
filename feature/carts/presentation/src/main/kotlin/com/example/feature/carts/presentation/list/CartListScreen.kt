package com.example.feature.carts.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.shared.designsystem.component.AppButton
import com.example.core.shared.designsystem.theme.AppTheme
import com.example.feature.carts.domain.Cart

@Composable
fun CartListScreen(
    state: CartListUiState,
    onAction: (CartListAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onCartClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading && state.carts.isEmpty() ->
                    Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

                state.errorMessage != null && state.carts.isEmpty() ->
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.errorMessage, color = AppTheme.colors.error)
                            AppButton("Coba lagi", { onAction(CartListAction.Retry) }, Modifier.padding(top = AppTheme.dimens.space))
                        }
                    }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(AppTheme.dimens.space),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
                ) {
                    items(state.carts, key = { it.id }) { cart -> CartRow(cart) { onCartClick(cart.id) } }
                }
            }
        }
    }
}

@Composable
private fun CartRow(cart: Cart, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.space),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceXs),
        ) {
            Text("Keranjang #${cart.id}", style = AppTheme.typography.titleMedium)
            Text("User ${cart.userId}  ·  ${cart.totalProducts} produk", style = AppTheme.typography.bodySmall, color = AppTheme.colors.onSurfaceVariant)
            Text("Total: $ ${cart.total}", style = AppTheme.typography.labelLarge, color = AppTheme.colors.primary)
        }
    }
}
