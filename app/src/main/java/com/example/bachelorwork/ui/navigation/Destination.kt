package com.example.bachelorwork.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


data class TopLevelRoute<T : Destination>(val route: T)

val topLevelRoutes = listOf(
    TopLevelRoute(Destination.Home),
    TopLevelRoute(Destination.Warehouse),
    TopLevelRoute(Destination.ManageOrders),
    TopLevelRoute(Destination.More),
)

sealed class Destination {

    @Serializable
    data object Home : Destination()

    @Serializable
    data object Warehouse : Destination()

    @Serializable
    data object More : Destination()


    @Serializable
    data object ManageOrders : Destination()

    @Serializable
    data object ManageUsers : Destination()


    @Serializable
    data object CreateProduct : Destination()

    @Serializable
    data object CreateNewUser : Destination()

    @Serializable
    data object CreateOrder : Destination()


    @Serializable
    data class ProductDetail(val id: Int) : Destination()

    @Serializable
    data class OrderDetail(val id: Int) : Destination()


    @Serializable
    data class EditProduct(val id: Int) : Destination()

    @Serializable
    data class EditOrder(val id: Int) : Destination()

    @Serializable
    data class EditUser(val id: Int) : Destination()


    companion object {
        inline fun <reified T : Destination> from(savedStateHandle: SavedStateHandle): T =
            savedStateHandle.toRoute<T>()
    }
}

