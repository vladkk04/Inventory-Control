package com.example.bachelorwork.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.bachelorwork.data.remote.auth.AuthApi
import com.example.bachelorwork.data.remote.repositories.AuthRepositoryImpl
import com.example.bachelorwork.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    @ViewModelScoped
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(api: AuthApi, preferences: DataStore<Preferences>): AuthRepository =
        AuthRepositoryImpl(api, preferences)


}