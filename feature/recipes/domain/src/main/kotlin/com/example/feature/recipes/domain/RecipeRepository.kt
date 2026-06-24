package com.example.feature.recipes.domain

interface RecipeRepository {
    suspend fun getRecipes(limit: Int = 20): List<Recipe>
    suspend fun searchRecipes(query: String): List<Recipe>
    suspend fun getRecipe(id: Int): Recipe
}
