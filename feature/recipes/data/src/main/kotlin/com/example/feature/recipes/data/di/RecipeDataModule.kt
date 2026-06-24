package com.example.feature.recipes.data.di

import com.example.feature.recipes.data.RecipeRepositoryImpl
import com.example.feature.recipes.data.remote.RecipeRemoteSource
import com.example.feature.recipes.data.remote.RecipeRemoteSourceImpl
import com.example.feature.recipes.domain.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RecipeDataModule {
    @Binds @Singleton
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Binds
    abstract fun bindRecipeRemoteSource(impl: RecipeRemoteSourceImpl): RecipeRemoteSource
}
