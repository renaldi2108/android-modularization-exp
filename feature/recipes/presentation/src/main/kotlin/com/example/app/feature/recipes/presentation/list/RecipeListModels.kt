package com.example.app.feature.recipes.presentation.list

import androidx.compose.runtime.Immutable
import com.example.app.feature.recipes.domain.Recipe
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class RecipeListUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val recipes: ImmutableList<Recipe> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface RecipeListAction {
    data class QueryChanged(val value: String) : RecipeListAction
    data object Submit : RecipeListAction
    data object Retry : RecipeListAction
}
