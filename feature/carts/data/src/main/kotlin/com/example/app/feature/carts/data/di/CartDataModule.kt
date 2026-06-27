package com.example.app.feature.carts.data.di

import com.example.app.feature.carts.data.CartRepositoryImpl
import com.example.app.feature.carts.data.remote.CartRemoteSource
import com.example.app.feature.carts.data.remote.CartRemoteSourceImpl
import com.example.app.feature.carts.domain.CartRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CartDataModule {
    @Binds @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds
    abstract fun bindCartRemoteSource(impl: CartRemoteSourceImpl): CartRemoteSource
}
