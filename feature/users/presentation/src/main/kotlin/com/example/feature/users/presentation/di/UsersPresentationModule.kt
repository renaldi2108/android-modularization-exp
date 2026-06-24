package com.example.feature.users.presentation.di

import com.example.core.utils.navigation.FeatureNavGraph
import com.example.feature.users.presentation.navigation.usersNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object UsersNavModule {
    @Provides
    @IntoSet
    fun provideUsersNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.usersNavGraph(navController)
    }
}
