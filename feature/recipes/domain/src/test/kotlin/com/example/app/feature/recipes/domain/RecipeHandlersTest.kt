package com.example.app.feature.recipes.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeHandlersTest {
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

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = RecipeListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(RecipeListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = RecipeListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<RecipesEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is RecipeListState.Error)
        assertEquals("boom", (state as RecipeListState.Error).message)
        assertTrue(events.any { it is RecipesEvent.ShowError })
    }
    @Test
    fun `search dengan query memanggil pencarian`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = RecipeListHandler(repo, backgroundScope)
        handler.search("kopi")
        advanceUntilIdle()
        assertEquals("kopi", repo.searchedQuery)
        assertEquals(RecipeListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `search dengan query kosong memuat semua`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = RecipeListHandler(repo, backgroundScope)
        handler.search("   ")
        advanceUntilIdle()
        assertEquals(null, repo.searchedQuery)
        assertEquals(RecipeListState.Success(listOf(sample)), handler.state.value)
    }
    @Test
    fun `detail load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = RecipeDetailHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        assertEquals(RecipeDetailState.Success(sample), handler.state.value)
    }

    @Test
    fun `detail load gagal menghasilkan Error`() = runTest(dispatcher) {
        val handler = RecipeDetailHandler(FakeRepository(error = IllegalStateException("rusak")), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is RecipeDetailState.Error)
        assertEquals("rusak", (state as RecipeDetailState.Error).message)
    }
}
