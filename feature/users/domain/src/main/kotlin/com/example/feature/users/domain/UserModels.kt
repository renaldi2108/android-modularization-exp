package com.example.feature.users.domain

data class AppUser(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val image: String,
    val age: Int,
    val university: String,
) {
    val fullName: String get() = "$firstName $lastName".trim()
}

sealed interface UserListState {
    data object Loading : UserListState
    data class Success(val users: List<AppUser>) : UserListState
    data class Error(val message: String) : UserListState
}

sealed interface UserDetailState {
    data object Loading : UserDetailState
    data class Success(val user: AppUser) : UserDetailState
    data class Error(val message: String) : UserDetailState
}

sealed interface UsersEvent {
    data class ShowError(val message: String) : UsersEvent
}
