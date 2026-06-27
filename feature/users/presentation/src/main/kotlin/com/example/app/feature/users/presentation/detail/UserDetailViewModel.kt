package com.example.app.feature.users.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.users.domain.UserDetailHandler
import com.example.app.feature.users.domain.UserDetailState
import com.example.app.feature.users.domain.UserRepository
import com.example.app.feature.users.domain.UsersEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    repository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<UserDetailUiState, UserDetailAction>(), EventViewModel<UsersEvent> {

    private val userId: Int = checkNotNull(savedStateHandle.get<String>("id")).toInt()
    private val handler = UserDetailHandler(repository, viewModelScope)
    override val events: Flow<UsersEvent> = handler.events

    override fun initialState() = UserDetailUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<UserDetailUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                UserDetailState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is UserDetailState.Success -> current.copy(isLoading = false, user = domain.user, errorMessage = null)
                is UserDetailState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load(userId) }

    override fun onAction(action: UserDetailAction) = when (action) {
        UserDetailAction.Retry -> { handler.load(userId); Unit }
    }
}
