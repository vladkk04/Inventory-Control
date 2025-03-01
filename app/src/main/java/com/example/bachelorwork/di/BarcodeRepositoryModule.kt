package com.example.bachelorwork.di

import android.content.Context
import com.example.bachelorwork.data.repositories.BarcodeScannerRepositoryImpl
import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object BarcodeRepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideBarcodeScannerRepository(
        @ApplicationContext applicationContext: Context
    ): BarcodeScannerRepository = BarcodeScannerRepositoryImpl(applicationContext)
}