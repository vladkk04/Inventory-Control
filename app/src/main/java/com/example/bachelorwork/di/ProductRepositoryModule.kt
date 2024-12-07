package com.example.bachelorwork.di

import com.example.bachelorwork.data.local.AppDatabase
import com.example.bachelorwork.data.local.repository.ProductCategoryRepositoryImpl
import com.example.bachelorwork.data.local.repository.ProductRepositoryImpl
import com.example.bachelorwork.domain.repository.ProductCategoryRepository
import com.example.bachelorwork.domain.repository.ProductRepository
import com.example.bachelorwork.domain.usecase.product.CreateProductUseCase
import com.example.bachelorwork.domain.usecase.product.DeleteProductUseCase
import com.example.bachelorwork.domain.usecase.product.GetProductsUseCase
import com.example.bachelorwork.domain.usecase.product.ProductUseCases
import com.example.bachelorwork.domain.usecase.product.UpdateProductUseCase
import com.example.bachelorwork.domain.usecase.productCategory.CreateProductCategoryUseCase
import com.example.bachelorwork.domain.usecase.productCategory.DeleteProductCategoryUseCase
import com.example.bachelorwork.domain.usecase.productCategory.GetProductCategoriesUseCase
import com.example.bachelorwork.domain.usecase.productCategory.ProductCategoryUseCases
import com.example.bachelorwork.domain.usecase.productCategory.UpdateProductCategoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ProductRepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideProductRepository(db: AppDatabase): ProductRepository =
        ProductRepositoryImpl(db.productDao)

    @Provides
    @ViewModelScoped
    fun provideProductUseCases(repository: ProductRepository): ProductUseCases =
        ProductUseCases(
            createProduct = CreateProductUseCase(repository),
            updateProduct = UpdateProductUseCase(repository),
            deleteProduct = DeleteProductUseCase(repository),
            getProducts = GetProductsUseCase(repository)
        )

    @Provides
    @ViewModelScoped
    fun provideProductCategoryRepository(db: AppDatabase): ProductCategoryRepository =
        ProductCategoryRepositoryImpl(db.productCategoryDao)

    @Provides
    @ViewModelScoped
    fun provideProductCategoryUseCases(repository: ProductCategoryRepository): ProductCategoryUseCases =
        ProductCategoryUseCases(
            createCategory = CreateProductCategoryUseCase(repository),
            deleteCategory = DeleteProductCategoryUseCase(repository),
            updateCategory = UpdateProductCategoryUseCase(repository),
            getCategories = GetProductCategoriesUseCase(repository)
        )
}