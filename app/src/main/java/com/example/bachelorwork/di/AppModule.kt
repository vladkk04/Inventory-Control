package com.example.bachelorwork.di

import android.content.Context
import com.example.bachelorwork.data.local.AppDatabase
import com.example.bachelorwork.data.repository.BarcodeScannerRepositoryImpl
import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import com.example.bachelorwork.ui.navigation.NavigatorImpl
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
    ): AppDatabase = AppDatabase.getInstance(applicationContext)

    @Provides
    @Singleton
    fun provideNavigator(): Navigator = NavigatorImpl(startDestination = Destination.Home)

    @Provides
    @Singleton
    fun provideBarcodeScannerRepository(
        @ApplicationContext applicationContext: Context
    ): BarcodeScannerRepository {
        return BarcodeScannerRepositoryImpl(applicationContext)
    }

}