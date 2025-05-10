package com.example.inventorycotrol.di

import com.example.inventorycotrol.data.remote.services.AuthApiService
import com.example.inventorycotrol.data.remote.services.RefreshApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideAuthApiService(@AuthenticatedRetrofit retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideRefreshTokenApiService(@TokenRefreshRetrofit retrofit: Retrofit): RefreshApiService =
        retrofit.create(RefreshApiService::class.java)

}