package com.example.app.feature.auth.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthHandlerTest {
    private val dispatcher = UnconfinedTestDispatcher()

    private val sampleUser = User("1", "Eve", "eve@example.com", null, "tok-1")

    private class FakeAuthRepository(
        private val onLogin: (suspend (String, String) -> User)? = null,
    ) : AuthRepository {
        var current: User? = null
        var loggedOut = false
        override suspend fun loginWithEmail(email: String, password: String): User =
            requireNotNull(onLogin) { "onLogin tidak diset" }.invoke(email, password)
        override suspend fun logout() { loggedOut = true; current = null }
        override fun getCurrentUser(): User? = current
        override fun observeUser(): Flow<User?> = flowOf(current)
        override fun getToken(): String? = current?.token
    }

    @Test
    fun `login sukses memancarkan Success dan NavigateToDashboard`() = runTest(dispatcher) {
        val repo = FakeAuthRepository(onLogin = { _, _ -> sampleUser })
        val handler = AuthHandler(repo, backgroundScope)
        val events = mutableListOf<AuthEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)

        handler.loginWithEmail("eve@example.com", "secret123")
        advanceUntilIdle()

        assertEquals(AuthState.Success(sampleUser), handler.state.value)
        assertTrue(AuthEvent.NavigateToDashboard in events)
    }

    @Test
    fun `login gagal memancarkan Error dan ShowError`() = runTest(dispatcher) {
        val repo = FakeAuthRepository(onLogin = { _, _ -> throw IllegalStateException("boom") })
        val handler = AuthHandler(repo, backgroundScope)
        val events = mutableListOf<AuthEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)

        handler.loginWithEmail("a@b.com", "x")
        advanceUntilIdle()

        val state = handler.state.value
        assertTrue(state is AuthState.Error)
        assertEquals("boom", (state as AuthState.Error).message)
        assertTrue(events.any { it is AuthEvent.ShowError })
    }

    @Test
    fun `logout mengembalikan ke Idle dan SessionExpired`() = runTest(dispatcher) {
        val repo = FakeAuthRepository().apply { current = sampleUser }
        val handler = AuthHandler(repo, backgroundScope)
        val events = mutableListOf<AuthEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)

        handler.logout()
        advanceUntilIdle()

        assertEquals(AuthState.Idle, handler.state.value)
        assertTrue(repo.loggedOut)
        assertTrue(AuthEvent.SessionExpired in events)
    }

    @Test
    fun `checkSession Success bila ada user, Idle bila tidak`() = runTest(dispatcher) {
        val repo = FakeAuthRepository()
        val handler = AuthHandler(repo, backgroundScope)

        handler.checkSession()
        assertEquals(AuthState.Idle, handler.state.value)

        repo.current = sampleUser
        handler.checkSession()
        assertEquals(AuthState.Success(sampleUser), handler.state.value)
    }
}
