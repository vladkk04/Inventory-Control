package com.example.bachelorwork.di

import android.content.Context
import com.example.bachelorwork.data.repository.BarcodeScannerRepositoryImpl
import com.example.bachelorwork.data.repository.ProductCategoryRepositoryImpl
import com.example.bachelorwork.domain.repository.BarcodeScannerRepository
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import com.example.bachelorwork.domain.usecase.category.CategoryUseCases
import com.example.bachelorwork.domain.usecase.category.CreateCategoryUseCase
import com.example.bachelorwork.domain.usecase.category.DeleteCategoryUseCase
import com.example.bachelorwork.domain.usecase.category.GetCategoryUseCase
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
        return BarcodeScannerRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(): ProductCategoryRepository = ProductCategoryRepositoryImpl()

    @Provides
    @Singleton
    fun provideCategoryUseCases(repository: ProductCategoryRepository): CategoryUseCases {
        return CategoryUseCases(
            createCategory = CreateCategoryUseCase(repository),
            getCategory = GetCategoryUseCase(repository),
            deleteCategory = DeleteCategoryUseCase(repository)
        )
    }

 }