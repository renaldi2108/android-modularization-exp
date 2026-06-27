package com.example.app.feature.quotes.data.di

import com.example.app.feature.quotes.data.QuoteRepositoryImpl
import com.example.app.feature.quotes.data.remote.QuoteRemoteSource
import com.example.app.feature.quotes.data.remote.QuoteRemoteSourceImpl
import com.example.app.feature.quotes.domain.QuoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class QuoteDataModule {
    @Binds @Singleton
    abstract fun bindQuoteRepository(impl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    abstract fun bindQuoteRemoteSource(impl: QuoteRemoteSourceImpl): QuoteRemoteSource
}
