package com.example.app.feature.recipes.domain

import com.example.app.core.common.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RecipeListHandler(
    private val repo: RecipeRepository,
    scope: CoroutineScope,
) : BaseHandler<RecipeListState, RecipesEvent>(RecipeListState.Loading, scope) {

    fun load() = scope.launch {
        setState { RecipeListState.Loading }
        runCatching { repo.getRecipes() }
            .onSuccess { setState { RecipeListState.Success(it) } }
            .onFailure { fail(it) }
    }

    fun search(query: String) = scope.launch {
        setState { RecipeListState.Loading }
        runCatching { if (query.isBlank()) repo.getRecipes() else repo.searchRecipes(query) }
            .onSuccess { setState { RecipeListState.Success(it) } }
            .onFailure { fail(it) }
    }

    private suspend fun fail(e: Throwable) {
        val msg = e.message ?: "Gagal memuat resep"
        setState { RecipeListState.Error(msg) }
        emitEvent(RecipesEvent.ShowError(msg))
    }
}

class RecipeDetailHandler(
    private val repo: RecipeRepository,
    scope: CoroutineScope,
) : BaseHandler<RecipeDetailState, RecipesEvent>(RecipeDetailState.Loading, scope) {

    fun load(id: Int) = scope.launch {
        setState { RecipeDetailState.Loading }
        runCatching { repo.getRecipe(id) }
            .onSuccess { setState { RecipeDetailState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat detail"
                setState { RecipeDetailState.Error(msg) }
                emitEvent(RecipesEvent.ShowError(msg))
            }
    }
}
