package com.example.app.feature.users.data

import com.example.app.core.common.result.Result
import com.example.app.feature.users.data.remote.UserDto
import com.example.app.feature.users.data.remote.UsersResponse
import com.example.app.feature.users.data.remote.UserRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserRepositoryImplTest {
    private val dto = UserDto(id = 1, firstName = "Eve", lastName = "Stone", email = "eve@example.com", phone = "0812", image = "img", age = 22, university = "ITB")

    private class FakeRemote(
        var listResult: Result<UsersResponse> = Result.Success(UsersResponse()),
        var searchResult: Result<UsersResponse> = Result.Success(UsersResponse()),
        var detailResult: Result<UserDto> = Result.Success(UserDto(id = 0)),
    ) : UserRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getUsers(limit: Int) = listResult
        override suspend fun searchUsers(query: String): Result<UsersResponse> { searchedQuery = query; return searchResult }
        override suspend fun getUser(id: Int) = detailResult
    }

    @Test
    fun `getUsers sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(UsersResponse(users = listOf(dto))))
        val repo = UserRepositoryImpl(remote)
        val result = repo.getUsers()
        assertEquals(1, result.size)
        assertEquals("Eve Stone", result[0].fullName)
    }

    @Test
    fun `getUsers error melempar IllegalStateException berisi message`() = runTest {
        val repo = UserRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getUsers() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getUsers error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = UserRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getUsers() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
    @Test
    fun `searchUsers meneruskan query dan memetakan hasil`() = runTest {
        val remote = FakeRemote(searchResult = Result.Success(UsersResponse(users = listOf(dto))))
        val repo = UserRepositoryImpl(remote)
        val result = repo.searchUsers("kopi")
        assertEquals("kopi", remote.searchedQuery)
        assertEquals(1, result.size)
    }
    @Test
    fun `getUser sukses memetakan dto ke domain`() = runTest {
        val repo = UserRepositoryImpl(FakeRemote(detailResult = Result.Success(dto)))
        val result = repo.getUser(1)
        assertEquals(1, result.id)
        assertEquals("Eve Stone", result.fullName)
    }

    @Test
    fun `getUser error melempar IllegalStateException`() = runTest {
        val repo = UserRepositoryImpl(FakeRemote(detailResult = Result.Error("tidak ditemukan")))
        val thrown = runCatching { repo.getUser(1) }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("tidak ditemukan", thrown!!.message)
    }
}
