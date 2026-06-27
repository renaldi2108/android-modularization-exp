package com.example.app.feature.recipes.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.app.feature.recipes.domain.Recipe
import com.example.app.feature.recipes.domain.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Recipe(id = 1, name = "Nasi", cuisine = "Asia", difficulty = "Easy", prepTimeMinutes = 5, cookTimeMinutes = 10, servings = 2, rating = 4.0, ingredients = listOf("beras"), instructions = listOf("masak"))

    private class FakeRepository(
        private val items: List<Recipe> = emptyList(),
        private val error: Throwable? = null,
    ) : RecipeRepository {
        var searchedQuery: String? = null
        override suspend fun getRecipes(limit: Int): List<Recipe> { error?.let { throw it }; return items }
        override suspend fun searchRecipes(query: String): List<Recipe> { searchedQuery = query; error?.let { throw it }; return items }
        override suspend fun getRecipe(id: Int): Recipe { error?.let { throw it }; return items.first() }
    }

    private fun handle() = SavedStateHandle(mapOf("id" to "1"))

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat detail sukses memperbarui state`() = runTest(dispatcher) {
        val vm = RecipeDetailViewModel(FakeRepository(items = listOf(sample)), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(sample, s.recipe)
    }

    @Test
    fun `memuat detail gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = RecipeDetailViewModel(FakeRepository(error = IllegalStateException("rusak")), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("rusak", vm.uiState.value.errorMessage)
    }
}
