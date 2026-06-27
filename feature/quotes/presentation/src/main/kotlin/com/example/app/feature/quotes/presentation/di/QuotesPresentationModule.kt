package com.example.app.feature.quotes.presentation.di

import com.example.app.core.utils.navigation.FeatureNavGraph
import com.example.app.feature.quotes.presentation.navigation.quotesNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object QuotesNavModule {
    @Provides
    @IntoSet
    fun provideQuotesNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.quotesNavGraph(navController)
    }
}
