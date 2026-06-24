package com.example.feature.auth.data.di

import com.example.feature.auth.data.AuthRepositoryImpl
import com.example.feature.auth.data.local.AuthLocalSource
import com.example.feature.auth.data.local.AuthLocalSourceImpl
import com.example.feature.auth.data.remote.AuthRemoteSource
import com.example.feature.auth.data.remote.AuthRemoteSourceImpl
import com.example.feature.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthDataModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindAuthRemoteSource(impl: AuthRemoteSourceImpl): AuthRemoteSource

    @Binds
    @Singleton
    abstract fun bindAuthLocalSource(impl: AuthLocalSourceImpl): AuthLocalSource
}
