package com.example.inventorycotrol.di

import android.content.Context
import com.example.inventorycotrol.data.local.AppDatabase
import com.example.inventorycotrol.data.remote.authenticator.AuthAuthenticator
import com.example.inventorycotrol.data.remote.interceptors.TokenInterceptor
import com.example.inventorycotrol.data.remote.interceptors.TokenInterceptorType
import com.example.inventorycotrol.data.remote.repositories.AuthRemoteDataSourceImpl
import com.example.inventorycotrol.data.remote.repositories.organisation.ProductRemoteDataImpl
import com.example.inventorycotrol.data.remote.services.AuthApiService
import com.example.inventorycotrol.data.remote.services.ProductApiService
import com.example.inventorycotrol.data.remote.services.RefreshApiService
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.domain.repository.remote.ProductRemoteDataSource
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.AppNavigatorImpl
import com.example.inventorycotrol.ui.notification.ProductStockNotificationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention
@Qualifier
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthenticatedClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenRefreshClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthenticatedRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenRefreshRetrofit

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
    fun provideAuthDataStore(
        api: AuthApiService,
        dataStoreManager: DataStoreManager,
        refreshApiService: RefreshApiService
    ): AuthRemoteDataSource =
        AuthRemoteDataSourceImpl(api, refreshApiService, dataStoreManager)

    @Provides
    @Singleton
    fun provideProductStockNotificationService(@ApplicationContext context: Context): ProductStockNotificationService =
        ProductStockNotificationService(context)

    @Provides
    @Singleton
    fun provideProductRemoteDataSource(
        api: ProductApiService,
        dataStoreManager: DataStoreManager
    ): ProductRemoteDataSource = ProductRemoteDataImpl(api, dataStoreManager)

    @Provides
    @Singleton
    fun provideAppNavigator(dataStoreManager: DataStoreManager, authDataSource: AuthRemoteDataSource): AppNavigator =
        AppNavigatorImpl(dataStoreManager, authDataSource)


    @Provides
    @Singleton
    @AuthenticatedClient
    fun provideAccessOkHttpClient(authTokenManager: DataStoreManager, refreshApiService: RefreshApiService): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .authenticator(AuthAuthenticator(authTokenManager, refreshApiService))
            .addInterceptor(TokenInterceptor(authTokenManager, TokenInterceptorType.ACCESS))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @TokenRefreshClient
    fun provideRefreshOkHttpClient(authTokenManager: DataStoreManager): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(authTokenManager, TokenInterceptorType.REFRESH))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpMainClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @AuthenticatedRetrofit
    fun provideAuthenticatedRetrofit(@AuthenticatedClient okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.60:5000/"/*AppConstants.BASE_URL*/)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    @Provides
    @Singleton
    @TokenRefreshRetrofit
    fun provideTokenRefreshRetrofit(@TokenRefreshClient okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.60:5000/") //AppConstants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.60:5000/")/*AppConstants.BASE_URL)*/
        .client(okHttpClient)
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}
