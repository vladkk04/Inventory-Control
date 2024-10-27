package com.example.bachelorwork.di

import com.example.bachelorwork.data.local.ProductDatabase
import com.example.bachelorwork.data.local.repository.ProductRepositoryImpl
import com.example.bachelorwork.data.repository.ProductCategoryRepositoryImpl
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import com.example.bachelorwork.domain.repository.ProductRepository
import com.example.bachelorwork.domain.usecase.product.DeleteProductUseCase
import com.example.bachelorwork.domain.usecase.product.GetProductsUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.productCategory.CreateProductCategoryUseCase
import com.example.bachelorwork.domain.usecase.productCategory.DeleteProductCategoryUseCase
import com.example.bachelorwork.domain.usecase.productCategory.GetProductCategoriesUseCase
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.domain.usecase.productCategory.UpdateProductCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideProductRepository(db: ProductDatabase): ProductRepository =
        ProductRepositoryImpl(db.productDao)

    @Provides
    @Singleton
    fun provideProductUseCases(repository: ProductRepository): ProductUseCases =
        ProductUseCases(
            getProducts = GetProductsUseCase(repository),
            deleteProduct = DeleteProductUseCase(repository)
        )

    @Provides
    @Singleton
    fun provideCategoryRepository(): ProductCategoryRepository = ProductCategoryRepositoryImpl()

    @Provides
    @Singleton
    fun provideProductCategoryUseCases(repository: ProductCategoryRepository): ProductCategoryUseCases =
        ProductCategoryUseCases(
            createCategory = CreateProductCategoryUseCase(repository),
            deleteCategory = DeleteProductCategoryUseCase(repository),
            updateCategory = UpdateProductCategoryUseCase(repository),
            getAllCategory = GetProductCategoriesUseCase(repository)
        )
}