package com.example.app.feature.products.data.di

import com.example.app.feature.products.data.ProductRepositoryImpl
import com.example.app.feature.products.data.remote.ProductRemoteSource
import com.example.app.feature.products.data.remote.ProductRemoteSourceImpl
import com.example.app.feature.products.domain.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductDataModule {
    @Binds
    @Singleton
    abstract fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    @Binds
    abstract fun bindProductRemoteSource(impl: ProductRemoteSourceImpl): ProductRemoteSource
}
