package com.example.feature.products.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.example.core.shared.designsystem.component.AppButton
import com.example.core.shared.designsystem.component.AppTextField
import com.example.core.shared.designsystem.theme.AppTheme
import com.example.feature.products.domain.Product

@Composable
fun ProductListScreen(
    state: ProductListUiState,
    onAction: (ProductListAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onProductClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            AppTextField(
                value = state.query,
                onValueChange = { onAction(ProductListAction.QueryChanged(it)) },
                label = "Cari produk",
                leadingIcon = Icons.Outlined.Search,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onAction(ProductListAction.Submit) }),
                modifier = Modifier.padding(
                    horizontal = AppTheme.dimens.space,
                    vertical = AppTheme.dimens.spaceSm,
                ),
            )

            when {
                state.isLoading && state.products.isEmpty() ->
                    Centered { CircularProgressIndicator() }

                state.errorMessage != null && state.products.isEmpty() ->
                    Centered {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.errorMessage, color = AppTheme.colors.error)
                            AppButton(
                                text = "Coba lagi",
                                onClick = { onAction(ProductListAction.Retry) },
                                modifier = Modifier.padding(top = AppTheme.dimens.space),
                            )
                        }
                    }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(AppTheme.dimens.space),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
                ) {
                    items(state.products, key = { it.id }) { product ->
                        ProductRow(product = product, onClick = { onProductClick(product.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductRow(product: Product, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.space),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceXs),
        ) {
            Text(
                text = product.title,
                style = AppTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = product.category,
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.onSurfaceVariant,
            )
            Text(
                text = "$ ${product.price}   ·   ★ ${product.rating}",
                style = AppTheme.typography.labelLarge,
                color = AppTheme.colors.primary,
            )
        }
    }
}

@Composable
private fun Centered(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { content() }
}
