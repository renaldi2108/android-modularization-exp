package com.example.app.feature.recipes.data

import com.example.app.core.common.result.Result
import com.example.app.feature.recipes.data.remote.RecipeDto
import com.example.app.feature.recipes.data.remote.RecipesResponse
import com.example.app.feature.recipes.data.remote.RecipeRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeRepositoryImplTest {
    private val dto = RecipeDto(id = 1, name = "Nasi", cuisine = "Asia", difficulty = "Easy", prepTimeMinutes = 5, cookTimeMinutes = 10, servings = 2, rating = 4.0, ingredients = listOf("beras"), instructions = listOf("masak"))

    private class FakeRemote(
        var listResult: Result<RecipesResponse> = Result.Success(RecipesResponse()),
        var searchResult: Result<RecipesResponse> = Result.Success(RecipesResponse()),
        var detailResult: Result<RecipeDto> = Result.Success(RecipeDto(id = 0)),
    ) : RecipeRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getRecipes(limit: Int) = listResult
        override suspend fun searchRecipes(query: String): Result<RecipesResponse> { searchedQuery = query; return searchResult }
        override suspend fun getRecipe(id: Int) = detailResult
    }

    @Test
    fun `getRecipes sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(RecipesResponse(recipes = listOf(dto))))
        val repo = RecipeRepositoryImpl(remote)
        val result = repo.getRecipes()
        assertEquals(1, result.size)
        assertEquals("Nasi", result[0].name)
    }

    @Test
    fun `getRecipes error melempar IllegalStateException berisi message`() = runTest {
        val repo = RecipeRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getRecipes() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getRecipes error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = RecipeRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getRecipes() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
    @Test
    fun `searchRecipes meneruskan query dan memetakan hasil`() = runTest {
        val remote = FakeRemote(searchResult = Result.Success(RecipesResponse(recipes = listOf(dto))))
        val repo = RecipeRepositoryImpl(remote)
        val result = repo.searchRecipes("kopi")
        assertEquals("kopi", remote.searchedQuery)
        assertEquals(1, result.size)
    }
    @Test
    fun `getRecipe sukses memetakan dto ke domain`() = runTest {
        val repo = RecipeRepositoryImpl(FakeRemote(detailResult = Result.Success(dto)))
        val result = repo.getRecipe(1)
        assertEquals(1, result.id)
        assertEquals("Nasi", result.name)
    }

    @Test
    fun `getRecipe error melempar IllegalStateException`() = runTest {
        val repo = RecipeRepositoryImpl(FakeRemote(detailResult = Result.Error("tidak ditemukan")))
        val thrown = runCatching { repo.getRecipe(1) }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("tidak ditemukan", thrown!!.message)
    }
}
