package com.example.app.feature.users.presentation.list

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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserListViewModelTest {
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

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat data sukses memperbarui state`() = runTest(dispatcher) {
        val vm = UserListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(1, s.users.size)
        assertEquals(sample, s.users.first())
        assertNull(s.errorMessage)
    }

    @Test
    fun `memuat data gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = UserListViewModel(FakeRepository(error = IllegalStateException("boom")))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("boom", vm.uiState.value.errorMessage)
    }
    @Test
    fun `QueryChanged memperbarui query`() = runTest(dispatcher) {
        val vm = UserListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        vm.onAction(UserListAction.QueryChanged("kopi"))
        advanceUntilIdle()
        assertEquals("kopi", vm.uiState.value.query)
    }

    @Test
    fun `Submit memicu pencarian dengan query terkini`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val vm = UserListViewModel(repo)
        backgroundScope.launch { vm.uiState.collect {} }
        vm.onAction(UserListAction.QueryChanged("kopi"))
        vm.onAction(UserListAction.Submit)
        advanceUntilIdle()
        assertEquals("kopi", repo.searchedQuery)
    }
}
