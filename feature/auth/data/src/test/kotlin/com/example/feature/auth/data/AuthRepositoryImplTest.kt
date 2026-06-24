package com.example.feature.auth.data

import com.example.core.utils.result.Result
import com.example.feature.auth.data.local.AuthLocalSource
import com.example.feature.auth.data.remote.AuthRemoteSource
import com.example.feature.auth.data.remote.LoginResponse
import com.example.feature.auth.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthRepositoryImplTest {

    private val response = LoginResponse(
        id = 7,
        username = "emilys",
        email = "eve@example.com",
        firstName = "Eve",
        lastName = "Stone",
        image = "http://img/eve.png",
        accessToken = "tok-xyz",
    )

    private class FakeRemote(
        var loginResult: Result<LoginResponse>,
        var logoutResult: Result<Unit> = Result.Success(Unit),
    ) : AuthRemoteSource {
        var logoutCalled = false
        override suspend fun login(email: String, password: String) = loginResult
        override suspend fun logout(): Result<Unit> {
            logoutCalled = true
            return logoutResult
        }
    }

    private class FakeLocal : AuthLocalSource {
        val userState = MutableStateFlow<User?>(null)
        var tokenValue: String? = null
        var cleared = false
        override fun observeUser(): Flow<User?> = userState.asStateFlow()
        override fun getUser(): User? = userState.value
        override fun getToken(): String? = tokenValue
        override suspend fun saveUser(user: User) { userState.value = user }
        override suspend fun saveToken(token: String) { tokenValue = token }
        override suspend fun clearAll() { cleared = true; userState.value = null; tokenValue = null }
    }

    @Test
    fun `login sukses memetakan response ke User dan menyimpan ke local`() = runTest {
        val remote = FakeRemote(loginResult = Result.Success(response))
        val local = FakeLocal()
        val repo = AuthRepositoryImpl(remote, local)

        val user = repo.loginWithEmail("eve@example.com", "secret123")

        assertEquals("7", user.id)
        assertEquals("Eve Stone", user.fullName)
        assertEquals("eve@example.com", user.email)
        assertEquals("http://img/eve.png", user.photoUrl)
        assertEquals("tok-xyz", user.token)

        assertEquals(user, local.getUser())
        assertEquals("tok-xyz", local.getToken())
    }

    @Test
    fun `login gagal tanpa cause melempar IllegalStateException berisi message`() = runTest {
        val repo = AuthRepositoryImpl(FakeRemote(Result.Error("kredensial salah")), FakeLocal())

        val thrown = runCatching { repo.loginWithEmail("a@b.com", "x") }.exceptionOrNull()

        assertTrue(thrown is IllegalStateException)
        assertEquals("kredensial salah", thrown!!.message)
    }

    @Test
    fun `login gagal meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = AuthRepositoryImpl(FakeRemote(Result.Error("gagal", cause = boom)), FakeLocal())

        val thrown = runCatching { repo.loginWithEmail("a@b.com", "x") }.exceptionOrNull()

        assertSame(boom, thrown)
    }

    @Test
    fun `logout memanggil remote logout dan membersihkan local`() = runTest {
        val remote = FakeRemote(Result.Success(response))
        val local = FakeLocal().apply { tokenValue = "t"; userState.value = User("1", "x", "e", null, "t") }
        val repo = AuthRepositoryImpl(remote, local)

        repo.logout()

        assertTrue(remote.logoutCalled)
        assertTrue(local.cleared)
    }

    @Test
    fun `getToken dan getCurrentUser mendelegasikan ke local`() {
        val local = FakeLocal().apply { tokenValue = "abc" }
        val repo = AuthRepositoryImpl(FakeRemote(Result.Success(response)), local)

        assertEquals("abc", repo.getToken())
        assertNull(repo.getCurrentUser())
    }
}
