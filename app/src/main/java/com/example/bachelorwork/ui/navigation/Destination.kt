package com.example.bachelorwork.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable


data class TopLevelRoute<T : Destination>(val route: T)

val topLevelRoutes = listOf(
    TopLevelRoute(Destination.Home),
    TopLevelRoute(Destination.Warehouse),
    TopLevelRoute(Destination.Orders),
    TopLevelRoute(Destination.More),
)

sealed class Destination {

    @Serializable
    data object Home : Destination()

    @Serializable
    data object Warehouse : Destination()

    @Serializable
    data object Orders : Destination()

    @Serializable
    data object Order: Destination()

    @Serializable
    data object More : Destination()

    @Serializable
    data class ProductDetail(val id: Int) : Destination()

    @Serializable
    data class ManageOrder(val id: Int? = null): Destination()

    @Serializable
    data class ProductManage(val id: Int? = null) : Destination()

    @Serializable
    data object ManageUsers : Destination()

    @Serializable
    data object CreateNewUser: Destination()

    companion object {
        inline fun <reified T : Destination> from(savedStateHandle: SavedStateHandle): T =
            savedStateHandle.toRoute<T>()
    }
}

