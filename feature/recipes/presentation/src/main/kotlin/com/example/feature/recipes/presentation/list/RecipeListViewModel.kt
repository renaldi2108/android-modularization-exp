package com.example.feature.recipes.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.core.utils.uistate.MultiSourceUiStateHolderBuilder
import com.example.core.utils.viewmodel.BaseViewModel
import com.example.core.utils.viewmodel.EventViewModel
import com.example.feature.recipes.domain.RecipeListHandler
import com.example.feature.recipes.domain.RecipeListState
import com.example.feature.recipes.domain.RecipeRepository
import com.example.feature.recipes.domain.RecipesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    repository: RecipeRepository,
) : BaseViewModel<RecipeListUiState, RecipeListAction>(), EventViewModel<RecipesEvent> {

    private val handler = RecipeListHandler(repository, viewModelScope)
    override val events: Flow<RecipesEvent> = handler.events

    override fun initialState() = RecipeListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<RecipeListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                RecipeListState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is RecipeListState.Success -> current.copy(isLoading = false, recipes = domain.recipes.toImmutableList(), errorMessage = null)
                is RecipeListState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: RecipeListAction) = when (action) {
        is RecipeListAction.QueryChanged -> updateUi { copy(query = action.value) }
        RecipeListAction.Submit          -> { handler.search(uiState.value.query); Unit }
        RecipeListAction.Retry           -> { handler.load(); Unit }
    }
}
