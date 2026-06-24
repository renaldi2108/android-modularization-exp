package com.example.feature.users.domain

import com.example.core.utils.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserListHandler(
    private val repo: UserRepository,
    scope: CoroutineScope,
) : BaseHandler<UserListState, UsersEvent>(UserListState.Loading, scope) {

    fun load() = scope.launch {
        setState { UserListState.Loading }
        runCatching { repo.getUsers() }
            .onSuccess { setState { UserListState.Success(it) } }
            .onFailure { fail(it) }
    }

    fun search(query: String) = scope.launch {
        setState { UserListState.Loading }
        runCatching { if (query.isBlank()) repo.getUsers() else repo.searchUsers(query) }
            .onSuccess { setState { UserListState.Success(it) } }
            .onFailure { fail(it) }
    }

    private suspend fun fail(e: Throwable) {
        val msg = e.message ?: "Gagal memuat pengguna"
        setState { UserListState.Error(msg) }
        emitEvent(UsersEvent.ShowError(msg))
    }
}

class UserDetailHandler(
    private val repo: UserRepository,
    scope: CoroutineScope,
) : BaseHandler<UserDetailState, UsersEvent>(UserDetailState.Loading, scope) {

    fun load(id: Int) = scope.launch {
        setState { UserDetailState.Loading }
        runCatching { repo.getUser(id) }
            .onSuccess { setState { UserDetailState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat detail"
                setState { UserDetailState.Error(msg) }
                emitEvent(UsersEvent.ShowError(msg))
            }
    }
}
