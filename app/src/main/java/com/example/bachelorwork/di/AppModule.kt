package com.example.bachelorwork.di

import android.content.Context
import com.example.bachelorwork.data.repository.BarcodeScannerImplRepository
import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBarcodeScannerRepository(
        @ApplicationContext context: Context
    ): BarcodeScannerRepository {
        return BarcodeScannerImplRepository(context)
    }

}