package com.example.bachelorwork.di

import com.example.bachelorwork.data.local.AppDatabase
import com.example.bachelorwork.data.local.repository.OrderRepositoryImpl
import com.example.bachelorwork.domain.repository.OrderRepository
import com.example.bachelorwork.domain.usecase.order.CreateOrderUseCase
import com.example.bachelorwork.domain.usecase.order.DeleteOrderUseCase
import com.example.bachelorwork.domain.usecase.order.GetOrdersUseCase
import com.example.bachelorwork.domain.usecase.order.OrderUseCases
import com.example.bachelorwork.domain.usecase.order.UpdateOrderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object OrderRepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideOrderRepository(db: AppDatabase): OrderRepository =
        OrderRepositoryImpl(db.orderDao)

    @Provides
    @ViewModelScoped
    fun provideOrderUseCases(repository: OrderRepository): OrderUseCases =
        OrderUseCases(
            createOrder = CreateOrderUseCase(repository),
            updateOrder = UpdateOrderUseCase(repository),
            deleteOrder = DeleteOrderUseCase(repository),
            getOrders = GetOrdersUseCase(repository)
        )
}