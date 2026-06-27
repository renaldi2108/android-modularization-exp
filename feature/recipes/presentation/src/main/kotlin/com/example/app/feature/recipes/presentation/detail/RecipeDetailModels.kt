package com.example.app.feature.recipes.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.app.feature.recipes.domain.Recipe

@Immutable
data class RecipeDetailUiState(
    val isLoading: Boolean = false,
    val recipe: Recipe? = null,
    val errorMessage: String? = null,
)

sealed interface RecipeDetailAction {
    data object Retry : RecipeDetailAction
}
