package com.example.app.feature.users.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.app.feature.users.domain.AppUser
import com.example.app.feature.users.domain.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = AppUser(id = 1, firstName = "Eve", lastName = "Stone", email = "eve@example.com", phone = "0812", image = "img", age = 22, university = "ITB")

    private class FakeRepository(
        private val items: List<AppUser> = emptyList(),
        private val error: Throwable? = null,
    ) : UserRepository {
        var searchedQuery: String? = null
        override suspend fun getUsers(limit: Int): List<AppUser> { error?.let { throw it }; return items }
        override suspend fun searchUsers(query: String): List<AppUser> { searchedQuery = query; error?.let { throw it }; return items }
        override suspend fun getUser(id: Int): AppUser { error?.let { throw it }; return items.first() }
    }

    private fun handle() = SavedStateHandle(mapOf("id" to "1"))

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat detail sukses memperbarui state`() = runTest(dispatcher) {
        val vm = UserDetailViewModel(FakeRepository(items = listOf(sample)), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(sample, s.user)
    }

    @Test
    fun `memuat detail gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = UserDetailViewModel(FakeRepository(error = IllegalStateException("rusak")), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("rusak", vm.uiState.value.errorMessage)
    }
}
