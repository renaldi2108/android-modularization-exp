package com.example.feature.recipes.data

import com.example.core.utils.result.Result
import com.example.feature.recipes.data.remote.RecipeDto
import com.example.feature.recipes.data.remote.RecipeRemoteSource
import com.example.feature.recipes.domain.Recipe
import com.example.feature.recipes.domain.RecipeRepository
import javax.inject.Inject

private fun RecipeDto.toDomain() = Recipe(
    id = id, name = name, cuisine = cuisine, difficulty = difficulty,
    prepTimeMinutes = prepTimeMinutes, cookTimeMinutes = cookTimeMinutes,
    servings = servings, rating = rating, ingredients = ingredients, instructions = instructions,
)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class RecipeRepositoryImpl @Inject constructor(
    private val remote: RecipeRemoteSource,
) : RecipeRepository {
    override suspend fun getRecipes(limit: Int) = remote.getRecipes(limit).getOrThrow().recipes.map { it.toDomain() }
    override suspend fun searchRecipes(query: String) = remote.searchRecipes(query).getOrThrow().recipes.map { it.toDomain() }
    override suspend fun getRecipe(id: Int) = remote.getRecipe(id).getOrThrow().toDomain()
}
