package com.example.feature.auth.presentation.di

import com.example.core.utils.coroutine.ActivityRetainedCoroutineScope
import com.example.core.utils.navigation.FeatureNavGraph
import com.example.feature.auth.domain.AuthHandler
import com.example.feature.auth.domain.AuthRepository
import com.example.feature.auth.presentation.navigation.authNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object AuthNavModule {
    @Provides
    @IntoSet
    fun provideAuthNavGraph(): FeatureNavGraph = FeatureNavGraph { builder, navController ->
        builder.authNavGraph(navController)
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object AuthHandlerModule {
    @Provides
    @ActivityRetainedScoped
    fun provideAuthHandler(
        repo: AuthRepository,
        @ActivityRetainedCoroutineScope scope: CoroutineScope,
    ): AuthHandler = AuthHandler(repo, scope)
}
