package com.example.feature.todos.data.di

import com.example.feature.todos.data.TodoRepositoryImpl
import com.example.feature.todos.data.remote.TodoRemoteSource
import com.example.feature.todos.data.remote.TodoRemoteSourceImpl
import com.example.feature.todos.domain.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodoDataModule {
    @Binds @Singleton
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository

    @Binds
    abstract fun bindTodoRemoteSource(impl: TodoRemoteSourceImpl): TodoRemoteSource
}
