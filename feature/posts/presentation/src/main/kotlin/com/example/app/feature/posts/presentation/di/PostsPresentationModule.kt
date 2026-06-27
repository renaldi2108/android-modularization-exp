package com.example.app.feature.posts.presentation.di

import com.example.app.core.utils.navigation.FeatureNavGraph
import com.example.app.feature.posts.presentation.navigation.postsNavGraph
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
