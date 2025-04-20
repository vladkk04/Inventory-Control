package com.example.bachelorwork.di

import com.example.bachelorwork.data.remote.services.FileApiService
import com.example.bachelorwork.data.remote.services.OrderApiService
import com.example.bachelorwork.data.remote.services.OrganisationApiService
import com.example.bachelorwork.data.remote.services.OrganisationUserApiService
import com.example.bachelorwork.data.remote.services.ProductApiService
import com.example.bachelorwork.data.remote.services.ProductCategoryApiService
import com.example.bachelorwork.data.remote.services.ProductUpdateStockApiService
import com.example.bachelorwork.data.remote.services.ProfileApiService
import com.example.bachelorwork.data.remote.services.UserApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideOrganisationApiService(@AuthenticatedRetrofit retrofit: Retrofit): OrganisationApiService =
        retrofit.create(OrganisationApiService::class.java)

    @Provides
    @Singleton
    fun provideOrganisationUserApiService(@AuthenticatedRetrofit retrofit: Retrofit): OrganisationUserApiService =
        retrofit.create(OrganisationUserApiService::class.java)

    @Provides
    @Singleton
    fun provideProductApiService(@AuthenticatedRetrofit retrofit: Retrofit): ProductApiService =
        retrofit.create(ProductApiService::class.java)

    @Provides
    @Singleton
    fun provideProductUpdateStockApiService(@AuthenticatedRetrofit retrofit: Retrofit): ProductUpdateStockApiService =
        retrofit.create(ProductUpdateStockApiService::class.java)

    @Provides
    @Singleton
    fun provideOrderApiService(@AuthenticatedRetrofit retrofit: Retrofit): OrderApiService =
        retrofit.create(OrderApiService::class.java)

    @Provides
    @Singleton
    fun provideOrganisationProductCategoryApiService(@AuthenticatedRetrofit retrofit: Retrofit): ProductCategoryApiService =
        retrofit.create(ProductCategoryApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(@AuthenticatedRetrofit retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideProfileApiService(@AuthenticatedRetrofit retrofit: Retrofit): ProfileApiService =
        retrofit.create(ProfileApiService::class.java)

    @Provides
    @Singleton
    fun provideFileApiService(@AuthenticatedRetrofit retrofit: Retrofit): FileApiService =
        retrofit.create(FileApiService::class.java)



}