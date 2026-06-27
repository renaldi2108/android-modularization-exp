package com.example.app.feature.todos.data.di

import com.example.app.feature.todos.data.TodoRepositoryImpl
import com.example.app.feature.todos.data.remote.TodoRemoteSource
import com.example.app.feature.todos.data.remote.TodoRemoteSourceImpl
import com.example.app.feature.todos.domain.TodoRepository
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
