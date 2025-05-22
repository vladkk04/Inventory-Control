package com.example.inventorycotrol.ui.navigation

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import com.example.inventorycotrol.R
import com.example.inventorycotrol.ui.fragments.auth.AuthenticationFragment
import com.example.inventorycotrol.ui.fragments.auth.forgotPassword.ForgotPasswordFragment
import com.example.inventorycotrol.ui.fragments.auth.resetPassword.ResetPasswordFragment
import com.example.inventorycotrol.ui.fragments.auth.signIn.SignInFragment
import com.example.inventorycotrol.ui.fragments.auth.signUp.SignUpFragment
import com.example.inventorycotrol.ui.fragments.auth.verificationOtp.VerificationOtpFragment
import com.example.inventorycotrol.ui.fragments.changeEmail.ChangeEmailFragment
import com.example.inventorycotrol.ui.fragments.changePassword.ChangePasswordFragment
import com.example.inventorycotrol.ui.fragments.home.HomeFragment
import com.example.inventorycotrol.ui.fragments.more.MoreFragment
import com.example.inventorycotrol.ui.fragments.orders.detail.OrderDetailFragment
import com.example.inventorycotrol.ui.fragments.orders.manage.addProductToOrder.OrderAddProductFragment
import com.example.inventorycotrol.ui.fragments.orders.manage.create.OrderCreateFragment
import com.example.inventorycotrol.ui.fragments.orders.manage.discount.OrderManageDiscountFragment
import com.example.inventorycotrol.ui.fragments.orders.orderList.OrderListFragment
import com.example.inventorycotrol.ui.fragments.organisation.OrganisationListFragment
import com.example.inventorycotrol.ui.fragments.organisation.afterSignUp.InvitationListAfterSignUpFragment
import com.example.inventorycotrol.ui.fragments.organisation.edit.OrganisationEditFragment
import com.example.inventorycotrol.ui.fragments.organisation.manage.create.CreateOrganisationFragment
import com.example.inventorycotrol.ui.fragments.organisationInvitations.InvitationListFragment
import com.example.inventorycotrol.ui.fragments.organisationManage.OrganisationManageFragment
import com.example.inventorycotrol.ui.fragments.organisationSettings.OrganisationSettingsFragment
import com.example.inventorycotrol.ui.fragments.organisationUsers.assignRole.OrganisationUserAssignRoleFragment
import com.example.inventorycotrol.ui.fragments.organisationUsers.edit.OrganisationUserEditFragment
import com.example.inventorycotrol.ui.fragments.organisationUsers.list.OrganisationUserListFragment
import com.example.inventorycotrol.ui.fragments.organisationUsers.manage.OrganisationInvitationManagerFragment
import com.example.inventorycotrol.ui.fragments.productUpdateStock.ProductUpdateStockFragment
import com.example.inventorycotrol.ui.fragments.profile.ProfileFragment
import com.example.inventorycotrol.ui.fragments.profile.edit.ProfileEditFragment
import com.example.inventorycotrol.ui.fragments.reports.ReportsFragment
import com.example.inventorycotrol.ui.fragments.selectDatePeriod.SelectDatePeriodFragment
import com.example.inventorycotrol.ui.fragments.warehouse.filters.WarehouseFilterFragment
import com.example.inventorycotrol.ui.fragments.warehouse.productDetail.ProductDetailFragment
import com.example.inventorycotrol.ui.fragments.warehouse.productList.ProductListFragment
import com.example.inventorycotrol.ui.fragments.warehouse.productManage.productCreate.ProductCreateFragment
import com.example.inventorycotrol.ui.fragments.warehouse.productManage.productEdit.ProductEditFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AppNavigationGraph(
    private val lifecycleOwner: LifecycleOwner,
    private val navigator: AppNavigator,
    private val navController: NavController,
    private val drawerLayout: DrawerLayout? = null
) {
    init {
        createGraph()
        setupWithNavController(lifecycleOwner, navigator, navController)
    }

    private fun createGraph() {
        lifecycleOwner.lifecycleScope.launch {
            navigator.startDestination().collectLatest { startDestination ->
                navController.graph = navController.createGraph(
                    startDestination = startDestination,
                ) {
                    //Bottom Navigation
                    fragment<ProductListFragment, Destination.Warehouse>()
                    fragment<OrderListFragment, Destination.Orders>()
                    fragment<HomeFragment, Destination.Home>()
                    dialog<MoreFragment, Destination.More>()

                    //Auth Destinations
                    fragment<VerificationOtpFragment, Destination.VerificationOtp>()
                    fragment<AuthenticationFragment, Destination.Authentication>()
                    fragment<ForgotPasswordFragment, Destination.ForgotPassword>()
                    fragment<ResetPasswordFragment, Destination.ResetPassword>()
                    fragment<SignUpFragment, Destination.SignUp>()
                    fragment<SignInFragment, Destination.SignIn>()

                    //Profile

                    //Warehouse
                    fragment<WarehouseFilterFragment, Destination.WarehouseFilters>()
                    fragment<ProductDetailFragment, Destination.ProductDetail>()
                    dialog<ProductCreateFragment, Destination.CreateProduct>()
                    dialog<ProductEditFragment, Destination.EditProduct>()


                    //More Additional Fragments
                    fragment<OrganisationUserListFragment, Destination.ManageOrganisationUsers>()

                    //Side drawer
                    fragment<ReportsFragment, Destination.Reports>()

                    //Order
                    dialog<OrderManageDiscountFragment, Destination.OrderManageDiscount>()
                    dialog<OrderAddProductFragment, Destination.OrderProductSelector>()
                    fragment<OrderDetailFragment, Destination.OrderDetail>()
                    dialog<OrderCreateFragment, Destination.CreateOrder>()



                    dialog<ProductUpdateStockFragment, Destination.ProductStockUpdater>()

                    //Organisation User
                    dialog<OrganisationInvitationManagerFragment, Destination.OrganisationInvitationManager>()
                    dialog<OrganisationUserEditFragment, Destination.EditOrganisationUser>()
                    dialog<OrganisationUserAssignRoleFragment, Destination.AssignRoleOrganisationUser>()

                    dialog<SelectDatePeriodFragment, Destination.SelectDatePeriod>()

                    fragment<ProfileEditFragment, Destination.EditProfile>()

                    //Organization
                    fragment<CreateOrganisationFragment, Destination.CreateOrganisation>()
                    fragment<OrganisationSettingsFragment, Destination.ManageOrganisationSettings>()
                    fragment<OrganisationEditFragment, Destination.EditOrganisation>()

                    fragment<InvitationListAfterSignUpFragment, Destination.InvitationListAfterSignUp>()

                    fragment<ProfileFragment, Destination.Profile>()

                    fragment<ChangePasswordFragment, Destination.ChangePassword>()
                    fragment<ChangeEmailFragment, Destination.ChangeEmail>()

                    fragment<InvitationListFragment, Destination.Invitations>()


                    fragment<OrganisationListFragment, Destination.OrganisationList>()

                    fragment<OrganisationManageFragment, Destination.ManageOrganisation>()

                    //Auth

                }
            }
        }
    }

    private fun setupWithNavController(
        lifecycleOwner: LifecycleOwner,
        navigator: AppNavigator,
        navController: NavController
    ) {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            navigator.navigationActions.collectLatest { action ->
                when (action) {
                    is NavigationAction.Navigate -> {
                        navController.navigate(
                            route = action.destination,
                        ) {
                            val isAnyMoreDestination =
                                Destination.getMoreDestinations().any { action.destination == it }


                            val isDrawerDestinations =
                                Destination.getDrawerDestinations()
                                    .any { action.destination == it || action.destination::class.simpleName == it::class.simpleName }

                            anim {
                                this.enter =
                                    if (isAnyMoreDestination)
                                        R.anim.from_bottom
                                    else if (isDrawerDestinations)
                                        0
                                    else
                                        R.anim.from_right

                                this.exit =
                                    if (isAnyMoreDestination)
                                        R.anim.from_alpha
                                    else if (isDrawerDestinations)
                                        0
                                    else R.anim.to_left

                                this.popEnter =
                                    if (isAnyMoreDestination)
                                        R.anim.to_alpha
                                    else if (isDrawerDestinations)
                                        0
                                    else R.anim.from_left

                                this.popExit =
                                    if (isAnyMoreDestination)
                                        R.anim.from_alpha
                                    else if (isDrawerDestinations)
                                        0
                                    else R.anim.to_right
                            }
                            action.navOptions(this)
                        }
                    }

                    is NavigationAction.NavigateUp -> {
                        try {
                            action.args.forEach { (key, value) ->
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    key,
                                    value
                                )
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        navController.navigateUp()
                    }

                    NavigationAction.OpenNavigationDrawer -> {
                        drawerLayout?.open()
                    }
                }
            }
        }
    }
}
