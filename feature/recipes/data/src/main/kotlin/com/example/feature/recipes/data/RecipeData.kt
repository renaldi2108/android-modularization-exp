package com.example.feature.recipes.data

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.example.feature.recipes.domain.Recipe
import com.example.feature.recipes.domain.RecipeRepository
import com.squareup.moshi.JsonClass
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@JsonClass(generateAdapter = true)
data class RecipeDto(
    val id: Int,
    val name: String = "",
    val cuisine: String = "",
    val difficulty: String = "",
    val prepTimeMinutes: Int = 0,
    val cookTimeMinutes: Int = 0,
    val servings: Int = 0,
    val rating: Double = 0.0,
    val ingredients: List<String> = emptyList(),
    val instructions: List<String> = emptyList(),
)

@JsonClass(generateAdapter = true)
data class RecipesResponse(
    val recipes: List<RecipeDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface RecipeRemoteSource {
    suspend fun getRecipes(limit: Int): Result<RecipesResponse>
    suspend fun searchRecipes(query: String): Result<RecipesResponse>
    suspend fun getRecipe(id: Int): Result<RecipeDto>
}

class RecipeRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : RecipeRemoteSource {
    override suspend fun getRecipes(limit: Int): Result<RecipesResponse> =
        remote.fetch(ApiRequest.get("recipes", query = mapOf("limit" to limit.toString())))

    override suspend fun searchRecipes(query: String): Result<RecipesResponse> =
        remote.fetch(ApiRequest.get("recipes/search", query = mapOf("q" to query)))

    override suspend fun getRecipe(id: Int): Result<RecipeDto> =
        remote.fetch(ApiRequest.get("recipes/$id"))
}

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

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeDataModule {
    @Binds @Singleton
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Binds
    abstract fun bindRecipeRemoteSource(impl: RecipeRemoteSourceImpl): RecipeRemoteSource
}
