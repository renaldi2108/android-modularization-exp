package com.example.app.feature.users.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserHandlersTest {
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

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = UserListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(UserListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = UserListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<UsersEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is UserListState.Error)
        assertEquals("boom", (state as UserListState.Error).message)
        assertTrue(events.any { it is UsersEvent.ShowError })
    }
    @Test
    fun `search dengan query memanggil pencarian`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = UserListHandler(repo, backgroundScope)
        handler.search("kopi")
        advanceUntilIdle()
        assertEquals("kopi", repo.searchedQuery)
        assertEquals(UserListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `search dengan query kosong memuat semua`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = UserListHandler(repo, backgroundScope)
        handler.search("   ")
        advanceUntilIdle()
        assertEquals(null, repo.searchedQuery)
        assertEquals(UserListState.Success(listOf(sample)), handler.state.value)
    }
    @Test
    fun `detail load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = UserDetailHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        assertEquals(UserDetailState.Success(sample), handler.state.value)
    }

    @Test
    fun `detail load gagal menghasilkan Error`() = runTest(dispatcher) {
        val handler = UserDetailHandler(FakeRepository(error = IllegalStateException("rusak")), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is UserDetailState.Error)
        assertEquals("rusak", (state as UserDetailState.Error).message)
    }
}
