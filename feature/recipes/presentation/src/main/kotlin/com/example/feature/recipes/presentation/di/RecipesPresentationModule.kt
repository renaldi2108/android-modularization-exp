package com.example.feature.recipes.presentation.di

import com.example.core.utils.navigation.FeatureNavGraph
import com.example.feature.recipes.presentation.navigation.recipesNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object RecipesNavModule {
    @Provides
    @IntoSet
    fun provideRecipesNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.recipesNavGraph(navController)
    }
}
