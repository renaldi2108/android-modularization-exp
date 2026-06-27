package com.example.app.di

import com.example.app.BuildConfig
import com.example.app.core.network.NetworkConfig
import com.example.app.feature.auth.domain.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNetworkConfig(authRepository: Provider<AuthRepository>): NetworkConfig =
        NetworkConfig(
            baseUrl = BuildConfig.BASE_URL,
            isDebug = BuildConfig.DEBUG,
            dynamicHeaders = {
                val token = authRepository.get().getToken()
                if (token.isNullOrBlank()) emptyMap() else mapOf("Authorization" to "Bearer $token")
            }
        )
}
