package com.example.app.feature.posts.data

import com.example.app.core.common.result.Result
import com.example.app.feature.posts.data.remote.PostDto
import com.example.app.feature.posts.data.remote.ReactionsDto
import com.example.app.feature.posts.data.remote.PostsResponse
import com.example.app.feature.posts.data.remote.PostRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PostRepositoryImplTest {
    private val dto = PostDto(id = 1, title = "Judul", body = "Isi", tags = listOf("a"), userId = 9, views = 100, reactions = ReactionsDto(likes = 10, dislikes = 2))

    private class FakeRemote(
        var listResult: Result<PostsResponse> = Result.Success(PostsResponse()),
        var searchResult: Result<PostsResponse> = Result.Success(PostsResponse()),
        var detailResult: Result<PostDto> = Result.Success(PostDto(id = 0)),
    ) : PostRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getPosts(limit: Int) = listResult
        override suspend fun searchPosts(query: String): Result<PostsResponse> { searchedQuery = query; return searchResult }
        override suspend fun getPost(id: Int) = detailResult
    }

    @Test
    fun `getPosts sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(PostsResponse(posts = listOf(dto))))
        val repo = PostRepositoryImpl(remote)
        val result = repo.getPosts()
        assertEquals(1, result.size)
        assertEquals(10, result[0].likes)
        assertEquals(2, result[0].dislikes)
    }

    @Test
    fun `getPosts error melempar IllegalStateException berisi message`() = runTest {
        val repo = PostRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getPosts() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getPosts error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = PostRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getPosts() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
    @Test
    fun `searchPosts meneruskan query dan memetakan hasil`() = runTest {
        val remote = FakeRemote(searchResult = Result.Success(PostsResponse(posts = listOf(dto))))
        val repo = PostRepositoryImpl(remote)
        val result = repo.searchPosts("kopi")
        assertEquals("kopi", remote.searchedQuery)
        assertEquals(1, result.size)
    }
    @Test
    fun `getPost sukses memetakan dto ke domain`() = runTest {
        val repo = PostRepositoryImpl(FakeRemote(detailResult = Result.Success(dto)))
        val result = repo.getPost(1)
        assertEquals(1, result.id)
        assertEquals(10, result.likes)
    }

    @Test
    fun `getPost error melempar IllegalStateException`() = runTest {
        val repo = PostRepositoryImpl(FakeRemote(detailResult = Result.Error("tidak ditemukan")))
        val thrown = runCatching { repo.getPost(1) }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("tidak ditemukan", thrown!!.message)
    }
}
