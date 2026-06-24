package com.example.feature.carts.presentation.di

import com.example.core.utils.navigation.FeatureNavGraph
import com.example.feature.carts.presentation.navigation.cartsNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object CartsNavModule {
    @Provides
    @IntoSet
    fun provideCartsNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.cartsNavGraph(navController)
    }
}
