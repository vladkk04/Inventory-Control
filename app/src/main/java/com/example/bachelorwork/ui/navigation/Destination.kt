package com.example.bachelorwork.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import com.example.bachelorwork.ui.model.order.DiscountType
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
    data object ManageRoles : Destination()

    @Serializable
    data object CreateRole : Destination()

    @Serializable
    data object ChangePassword : Destination()

    @Serializable
    data object ChangeEmail : Destination()


    @Serializable
    data object StockIn : Destination()

    @Serializable
    data object StockOut : Destination()


    @Serializable
    data object InvitationListAfterSignUp : Destination()

    @Serializable
    data class ProductDetail(val id: String) : Destination()

    @Serializable
    data class OrderDetail(val id: String) : Destination()

    @Serializable
    data object WarehouseFilters : Destination()

    @Serializable
    data object CreateProduct : Destination()

    @Serializable
    data object OrganisationManageUser : Destination()

    @Serializable
    data object CreateOrder : Destination()

    @Serializable
    data object OrderAddProduct : Destination()

    @Serializable
    data class OrderEditAddedProduct(
        val id: Int,
        val quantity: Double,
        val rate: Double
    ) : Destination()

    @Serializable
    data class OrderManageDiscount(
        val subtotal: Double = 0.00,
        val discount: Double = 0.00,
        val discountType: DiscountType
    ) : Destination()

    @Serializable
    data class EditProduct(val id: String) : Destination()

    @Serializable
    data object Reports: Destination()

    @Serializable
    data class EditOrder(val id: String) : Destination()

    @Serializable
    data class EditUser(val id: Int) : Destination()

    @Serializable
    data class EditOrganisationRole(val id: String) : Destination()

    @Serializable
    data object UpdateProductsStock : Destination()

    //Organization
    @Serializable
    data object CreateOrganisation : Destination()

    @Serializable
    data object Invitations : Destination()

    @Serializable
    data object Profile : Destination()


    //Authentication

    @Serializable
    data object Authentication : Destination()

    @Serializable
    data object SignUp : Destination()

    @Serializable
    data object SignIn : Destination()

    @Serializable
    data object ForgotPassword : Destination()

    @Serializable
    data class VerificationOtp(val email: String) : Destination()

    @Serializable
    data class ResetPassword(val email: String) : Destination()

    @Serializable
    data class EditOrganisationUser(val organisationUserId: String, val name: String): Destination()

    @Serializable
    data class AssignRoleOrganisationUser(val organisationUserId: String, val organisationRole: OrganisationRole): Destination()

    @Serializable
    data object SelectDatePeriod: Destination()

    @Serializable
    data object OrganisationList : Destination()

    @Serializable
    data object ManageOrganisation : Destination()

    @Serializable
    data object Settings: Destination()

    @Serializable
    data object ProductUpdateStock : Destination()


    companion object {
        inline fun <reified T : Destination> from(savedStateHandle: SavedStateHandle): T =
            savedStateHandle.toRoute<T>()

        fun getMoreDestinations(): List<Destination> = listOf(ManageUsers, ManageRoles, ManageOrganisation)

        fun getTopLevelDestinations(): List<Destination> = listOf(Home, Warehouse, Orders, More)

        fun getDrawerDestinations(): List<Destination> = listOf(Home, Invitations, Settings, Reports)

    }


}






