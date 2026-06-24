package com.example.feature.products.presentation.di

import com.example.core.utils.navigation.FeatureNavGraph
import com.example.feature.products.presentation.navigation.productsNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object ProductsNavModule {
    @Provides
    @IntoSet
    fun provideProductsNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.productsNavGraph(navController)
    }
}
