package com.example.inventorycotrol.ui.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.ui.model.order.DiscountType
import kotlinx.serialization.Serializable

sealed class Destination {

    // Bottom Navigation Destinations

    @Serializable
    data object Warehouse : Destination()

    @Serializable
    data object Orders : Destination()

    @Serializable
    data object Home : Destination()

    @Serializable
    data object More : Destination()

    //Auth Destinations

    @Serializable
    data class VerificationOtp(val email: String) : Destination()

    @Serializable
    data class ResetPassword(val email: String) : Destination()

    @Serializable
    data object Authentication : Destination()

    @Serializable
    data object ForgotPassword : Destination()

    @Serializable
    data object SignUp : Destination()

    @Serializable
    data object SignIn : Destination()

    //Profile Destinations

    @Serializable
    data class EditProfile(val logo: String?, val fullName: String) : Destination()

    @Serializable
    data object ChangePassword : Destination()

    @Serializable
    data object ChangeEmail : Destination()

    @Serializable
    data object Profile : Destination()

    //Organisation Destinations

    @Serializable
    data class CreateOrganisation(val isNavigatedFromSidePanel: Boolean = false) : Destination()

    @Serializable
    data object OrganisationInvitationManager : Destination()

    @Serializable
    data object ManageOrganisationSettings : Destination()

    @Serializable
    data object ManageOrganisationUsers : Destination()

    @Serializable
    data object ManageOrganisation : Destination()

    @Serializable
    data object EditOrganisation : Destination()


    //After Sign Up Destinations

    @Serializable
    data object InvitationListAfterSignUp : Destination()

    @Serializable
    data object OrganisationList : Destination()

    // Warehouse Destinations

    @Serializable
    data class ProductDetail(val id: String) : Destination()

    @Serializable
    data class EditProduct(val id: String) : Destination()

    @Serializable
    data object SelectDatePeriod : Destination()

    @Serializable
    data object WarehouseFilters : Destination()

    @Serializable
    data object CreateProduct : Destination()

    //Order Destinations

    @Serializable
    data class OrderProductSelector(val currency: String) : Destination()

    @Serializable
    data object CreateOrder : Destination()

    @Serializable
    data class OrderDetail(
        val id: String,
        val currency: String
    ) : Destination()

    @Serializable
    data class EditOrder(val id: String) : Destination()

    @Serializable
    data class OrderManageDiscount(
        val subtotal: Double = 0.00,
        val discount: Double = 0.00,
        val discountType: DiscountType
    ) : Destination()

    //Side Drawer Destinations

    @Serializable
    data object Invitations : Destination()

    @Serializable
    data object Reports : Destination()

    //Manage Organisation Users

    @Serializable
    data class EditOrganisationUser(
        val organisationUserId: String,
        val name: String
    ) : Destination()

    @Serializable
    data class AssignRoleOrganisationUser(
        val organisationUserId: String,
        val organisationRole: OrganisationRole
    ) : Destination()

    // Update product stock

    @Serializable
    data object ProductStockUpdater : Destination()


    companion object {
        inline fun <reified T : Destination> from(savedStateHandle: SavedStateHandle): T = savedStateHandle.toRoute<T>()

        fun getMoreDestinations(): List<Destination> =
            listOf(ManageOrganisationUsers, ManageOrganisation, ManageOrganisationSettings)

        fun getTopLevelDestinations(): List<Destination> = listOf(Home, Warehouse, Orders, More)

        fun getDrawerDestinations(): List<Destination> =
            listOf(Home, Invitations, Reports, Profile, CreateOrganisation())
    }


}






