package com.example.app.feature.recipes.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.recipes.domain.RecipeDetailHandler
import com.example.app.feature.recipes.domain.RecipeDetailState
import com.example.app.feature.recipes.domain.RecipeRepository
import com.example.app.feature.recipes.domain.RecipesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    repository: RecipeRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<RecipeDetailUiState, RecipeDetailAction>(), EventViewModel<RecipesEvent> {

    private val recipeId: Int = checkNotNull(savedStateHandle.get<String>("id")).toInt()
    private val handler = RecipeDetailHandler(repository, viewModelScope)
    override val events: Flow<RecipesEvent> = handler.events

    override fun initialState() = RecipeDetailUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<RecipeDetailUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                RecipeDetailState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is RecipeDetailState.Success -> current.copy(isLoading = false, recipe = domain.recipe, errorMessage = null)
                is RecipeDetailState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load(recipeId) }

    override fun onAction(action: RecipeDetailAction) = when (action) {
        RecipeDetailAction.Retry -> { handler.load(recipeId); Unit }
    }
}
