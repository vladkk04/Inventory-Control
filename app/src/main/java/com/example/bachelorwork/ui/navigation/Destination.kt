package com.example.bachelorwork.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.bachelorwork.ui.model.order.create.DiscountType
import kotlinx.serialization.Serializable

sealed class Destination {

    @Serializable
    data object Home : Destination()

    @Serializable
    data object Warehouse : Destination()

    @Serializable
    data object Orders : Destination()

    @Serializable
    data object More : Destination()

    @Serializable
    data object ManageUsers : Destination()

    @Serializable
    data class ProductDetail(val id: Int) : Destination()

    @Serializable
    data class OrderDetail(val id: Int) : Destination()

    @Serializable
    data object CreateProduct : Destination()

    @Serializable
    data object CreateNewUser : Destination()

    @Serializable
    data object CreateOrder : Destination()

    @Serializable
    data object OrderAddProduct : Destination()

    @Serializable
    data class OrderEditAddedProduct(val id: Int, val quantity: Double, val rate: Double) : Destination()

    @Serializable
    data class OrderManageDiscount(
        val discount: Double = 0.00,
        val discountType: DiscountType
    ) : Destination()

    @Serializable
    data class EditProduct(val id: Int) : Destination()

    @Serializable
    data class EditOrder(val id: Int) : Destination()

    @Serializable
    data class EditUser(val id: Int) : Destination()

    companion object {
        inline fun <reified T : Destination> from(savedStateHandle: SavedStateHandle): T =
            savedStateHandle.toRoute<T>()

        fun getTopLevelDestinations(): List<Destination> = listOf(Home, Warehouse, Orders, More)
    }
}






