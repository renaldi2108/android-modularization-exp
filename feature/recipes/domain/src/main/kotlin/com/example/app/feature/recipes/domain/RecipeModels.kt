package com.example.app.feature.recipes.domain

data class Recipe(
    val id: Int,
    val name: String,
    val cuisine: String,
    val difficulty: String,
    val prepTimeMinutes: Int,
    val cookTimeMinutes: Int,
    val servings: Int,
    val rating: Double,
    val ingredients: List<String>,
    val instructions: List<String>,
)

sealed interface RecipeListState {
    data object Loading : RecipeListState
    data class Success(val recipes: List<Recipe>) : RecipeListState
    data class Error(val message: String) : RecipeListState
}

sealed interface RecipeDetailState {
    data object Loading : RecipeDetailState
    data class Success(val recipe: Recipe) : RecipeDetailState
    data class Error(val message: String) : RecipeDetailState
}

sealed interface RecipesEvent {
    data class ShowError(val message: String) : RecipesEvent
}
