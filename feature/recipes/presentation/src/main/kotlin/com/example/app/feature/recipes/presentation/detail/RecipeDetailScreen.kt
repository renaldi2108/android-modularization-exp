package com.example.app.feature.recipes.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.app.feature.recipes.domain.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    state: RecipeDetailUiState,
    onAction: (RecipeDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.recipe?.name ?: "Detail Resep", maxLines = 1) },
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
                state.recipe != null -> RecipeDetailContent(state.recipe)
                state.errorMessage != null -> Box(Modifier.fillMaxSize(), Alignment.Center) { Text(state.errorMessage, color = AppTheme.colors.error) }
            }
        }
    }
}

@Composable
private fun RecipeDetailContent(recipe: Recipe) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(AppTheme.dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceXs),
    ) {
        item {
            Text(
                "${recipe.cuisine} · ${recipe.difficulty} · ★ ${recipe.rating}",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.onSurfaceVariant,
            )
            Text(
                "Prep ${recipe.prepTimeMinutes} mnt · Masak ${recipe.cookTimeMinutes} mnt · ${recipe.servings} porsi",
                style = AppTheme.typography.bodySmall,
                color = AppTheme.colors.onSurfaceVariant,
            )
            Text("Bahan", style = AppTheme.typography.titleMedium, modifier = Modifier.padding(top = AppTheme.dimens.spaceSm))
        }
        items(recipe.ingredients) { ingredient ->
            Text("• $ingredient", style = AppTheme.typography.bodyMedium)
        }
        item {
            Text("Langkah", style = AppTheme.typography.titleMedium, modifier = Modifier.padding(top = AppTheme.dimens.spaceSm))
        }
        itemsIndexed(recipe.instructions) { index, step ->
            Text("${index + 1}. $step", style = AppTheme.typography.bodyMedium)
        }
    }
}
