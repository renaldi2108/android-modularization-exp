package com.example.app.feature.posts.data.di

import com.example.app.feature.posts.data.PostRepositoryImpl
import com.example.app.feature.posts.data.remote.PostRemoteSource
import com.example.app.feature.posts.data.remote.PostRemoteSourceImpl
import com.example.app.feature.posts.domain.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PostDataModule {
    @Binds @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun bindPostRemoteSource(impl: PostRemoteSourceImpl): PostRemoteSource
}
