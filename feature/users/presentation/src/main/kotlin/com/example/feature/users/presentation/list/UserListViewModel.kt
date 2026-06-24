package com.example.feature.users.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.core.utils.uistate.MultiSourceUiStateHolderBuilder
import com.example.core.utils.viewmodel.BaseViewModel
import com.example.core.utils.viewmodel.EventViewModel
import com.example.feature.users.domain.UserListHandler
import com.example.feature.users.domain.UserListState
import com.example.feature.users.domain.UserRepository
import com.example.feature.users.domain.UsersEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    repository: UserRepository,
) : BaseViewModel<UserListUiState, UserListAction>(), EventViewModel<UsersEvent> {

    private val handler = UserListHandler(repository, viewModelScope)
    override val events: Flow<UsersEvent> = handler.events

    override fun initialState() = UserListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<UserListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                UserListState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is UserListState.Success -> current.copy(isLoading = false, users = domain.users.toImmutableList(), errorMessage = null)
                is UserListState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: UserListAction) = when (action) {
        is UserListAction.QueryChanged -> updateUi { copy(query = action.value) }
        UserListAction.Submit          -> { handler.search(uiState.value.query); Unit }
        UserListAction.Retry           -> { handler.load(); Unit }
    }
}
