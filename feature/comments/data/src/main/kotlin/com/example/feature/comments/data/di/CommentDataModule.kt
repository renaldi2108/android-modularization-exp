package com.example.feature.comments.data.di

import com.example.feature.comments.data.CommentRepositoryImpl
import com.example.feature.comments.data.remote.CommentRemoteSource
import com.example.feature.comments.data.remote.CommentRemoteSourceImpl
import com.example.feature.comments.domain.CommentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommentDataModule {
    @Binds @Singleton
    abstract fun bindCommentRepository(impl: CommentRepositoryImpl): CommentRepository

    @Binds
    abstract fun bindCommentRemoteSource(impl: CommentRemoteSourceImpl): CommentRemoteSource
}
