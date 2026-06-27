package com.example.app.feature.comments.presentation.di

import com.example.app.core.utils.navigation.FeatureNavGraph
import com.example.app.feature.comments.presentation.navigation.commentsNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object CommentsNavModule {
    @Provides
    @IntoSet
    fun provideCommentsNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.commentsNavGraph(navController)
    }
}
