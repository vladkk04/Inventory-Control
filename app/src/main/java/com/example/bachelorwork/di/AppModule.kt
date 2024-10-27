package com.example.bachelorwork.di

import android.content.Context
import androidx.room.Room
import com.example.bachelorwork.data.local.ProductDatabase
import com.example.bachelorwork.data.repository.BarcodeScannerRepositoryImpl
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
    fun provideProductDatabase(
        @ApplicationContext applicationContext: Context
    ): ProductDatabase =
        Room.databaseBuilder(
            applicationContext,
            ProductDatabase::class.java,
            ProductDatabase.DATABASE_NAME
        ).build()


    @Provides
    @Singleton
    fun provideBarcodeScannerRepository(
        @ApplicationContext applicationContext: Context
    ): BarcodeScannerRepository {
        return BarcodeScannerRepositoryImpl(applicationContext)
    }

}