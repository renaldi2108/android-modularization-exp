package com.example.feature.auth.presentation.login

import com.example.feature.auth.domain.AuthHandler
import com.example.feature.auth.domain.AuthRepository
import com.example.feature.auth.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private class FakeAuthRepository : AuthRepository {
        var loginCalled = false
        var lastUsername: String? = null
        override suspend fun loginWithEmail(email: String, password: String): User {
            loginCalled = true
            lastUsername = email
            return User("1", "Eve Stone", "eve@example.com", null, "tok")
        }
        override suspend fun logout() {}
        override fun getCurrentUser(): User? = null
        override fun observeUser(): Flow<User?> = flowOf(null)
        override fun getToken(): String? = null
    }

    private lateinit var repo: FakeAuthRepository
    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repo = FakeAuthRepository()
        vm = LoginViewModel(AuthHandler(repo, TestScope(dispatcher)))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state awal default`() {
        val s = vm.uiState.value
        assertEquals("", s.username)
        assertFalse(s.isButtonEnabled)
        assertFalse(s.isLoading)
    }

    @Test
    fun `password pendek memunculkan error`() {
        vm.onAction(LoginUiAction.PasswordChanged("123"))
        assertNotNull(vm.uiState.value.passwordError)
    }

    @Test
    fun `username dan password valid mengaktifkan tombol`() {
        vm.onAction(LoginUiAction.UsernameChanged("emilys"))
        vm.onAction(LoginUiAction.PasswordChanged("emilyspass"))

        val s = vm.uiState.value
        assertNull(s.usernameError)
        assertNull(s.passwordError)
        assertTrue(s.isButtonEnabled)
    }

    @Test
    fun `toggle password membalik visibilitas`() {
        assertFalse(vm.uiState.value.passwordVisible)
        vm.onAction(LoginUiAction.PasswordToggled)
        assertTrue(vm.uiState.value.passwordVisible)
    }

    @Test
    fun `login click dengan input valid memanggil repository`() {
        vm.onAction(LoginUiAction.UsernameChanged("emilys"))
        vm.onAction(LoginUiAction.PasswordChanged("emilyspass"))
        vm.onAction(LoginUiAction.LoginClicked)

        assertTrue(repo.loginCalled)
        assertEquals("emilys", repo.lastUsername)
    }

    @Test
    fun `login click diabaikan saat input kosong`() {
        vm.onAction(LoginUiAction.LoginClicked)
        assertFalse(repo.loginCalled)
    }
}
