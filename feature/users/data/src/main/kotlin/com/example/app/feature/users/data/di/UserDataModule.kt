package com.example.app.feature.users.data.di

import com.example.app.feature.users.data.UserRepositoryImpl
import com.example.app.feature.users.data.remote.UserRemoteSource
import com.example.app.feature.users.data.remote.UserRemoteSourceImpl
import com.example.app.feature.users.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserDataModule {
    @Binds @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindUserRemoteSource(impl: UserRemoteSourceImpl): UserRemoteSource
}
