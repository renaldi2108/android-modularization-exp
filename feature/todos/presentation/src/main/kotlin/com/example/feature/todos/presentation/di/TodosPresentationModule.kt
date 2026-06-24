package com.example.feature.todos.presentation.di

import com.example.core.utils.navigation.FeatureNavGraph
import com.example.feature.todos.presentation.navigation.todosNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
object TodosNavModule {
    @Provides
    @IntoSet
    fun provideTodosNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.todosNavGraph(navController)
    }
}
