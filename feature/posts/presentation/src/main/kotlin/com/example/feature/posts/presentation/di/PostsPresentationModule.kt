package com.example.feature.posts.presentation.di

import com.example.core.utils.navigation.FeatureNavGraph
import com.example.feature.posts.presentation.navigation.postsNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object PostsNavModule {
    @Provides
    @IntoSet
    fun providePostsNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.postsNavGraph(navController)
    }
}
