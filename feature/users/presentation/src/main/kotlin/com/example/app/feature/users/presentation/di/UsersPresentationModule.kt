package com.example.app.feature.users.presentation.di

import com.example.app.core.utils.navigation.FeatureNavGraph
import com.example.app.feature.users.presentation.navigation.usersNavGraph
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
