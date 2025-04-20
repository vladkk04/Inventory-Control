package com.example.bachelorwork.ui.navigation

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import com.example.bachelorwork.R
import com.example.bachelorwork.ui.fragments.auth.AuthenticationFragment
import com.example.bachelorwork.ui.fragments.auth.forgotPassword.ForgotPasswordFragment
import com.example.bachelorwork.ui.fragments.auth.resetPassword.ResetPasswordFragment
import com.example.bachelorwork.ui.fragments.auth.signIn.SignInFragment
import com.example.bachelorwork.ui.fragments.auth.signUp.SignUpFragment
import com.example.bachelorwork.ui.fragments.auth.verificationOtp.VerificationOtpFragment
import com.example.bachelorwork.ui.fragments.changeEmail.ChangeEmailFragment
import com.example.bachelorwork.ui.fragments.changePassword.ChangePasswordFragment
import com.example.bachelorwork.ui.fragments.home.HomeFragment
import com.example.bachelorwork.ui.fragments.more.MoreFragment
import com.example.bachelorwork.ui.fragments.orders.detail.OrderDetailFragment
import com.example.bachelorwork.ui.fragments.orders.manage.addProductToOrder.OrderAddProductFragment
import com.example.bachelorwork.ui.fragments.orders.manage.create.OrderCreateFragment
import com.example.bachelorwork.ui.fragments.orders.manage.discount.OrderManageDiscountFragment
import com.example.bachelorwork.ui.fragments.orders.manage.edit.OrderEditFragment
import com.example.bachelorwork.ui.fragments.orders.orderList.OrderListFragment
import com.example.bachelorwork.ui.fragments.organisation.OrganisationListFragment
import com.example.bachelorwork.ui.fragments.organisation.afterSignUp.InvitationListAfterSignUpFragment
import com.example.bachelorwork.ui.fragments.organisation.manage.create.CreateOrganisationFragment
import com.example.bachelorwork.ui.fragments.organisationInvitations.InvitationListFragment
import com.example.bachelorwork.ui.fragments.organisationManage.OrganisationManageFragment

import com.example.bachelorwork.ui.fragments.organisationUsers.assignRole.OrganisationUserAssignRoleFragment
import com.example.bachelorwork.ui.fragments.organisationUsers.edit.OrganisationUserEditFragment
import com.example.bachelorwork.ui.fragments.organisationUsers.list.OrganisationUserListFragment
import com.example.bachelorwork.ui.fragments.organisationUsers.manage.OrganisationUserManageFragment
import com.example.bachelorwork.ui.fragments.productUpdateStock.ProductUpdateStockInFragment
import com.example.bachelorwork.ui.fragments.productUpdateStock.ProductUpdateStockOutFragment
import com.example.bachelorwork.ui.fragments.profile.ProfileFragment
import com.example.bachelorwork.ui.fragments.reports.ReportsFragment
import com.example.bachelorwork.ui.fragments.selectDatePeriod.SelectDatePeriodFragment
import com.example.bachelorwork.ui.fragments.settings.SettingsFragment
import com.example.bachelorwork.ui.fragments.warehouse.filters.WarehouseFilterFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.ProductDetailFragment
import com.example.bachelorwork.ui.fragments.warehouse.productList.ProductListFragment
import com.example.bachelorwork.ui.fragments.warehouse.productManage.productCreate.ProductCreateFragment
import com.example.bachelorwork.ui.fragments.warehouse.productManage.productEdit.ProductEditFragment
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import kotlinx.coroutines.Dispatchers


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
        lifecycleOwner.collectInLifecycle(navigator.startDestination()) { startDestination ->
            navController.graph = navController.createGraph(
                startDestination = startDestination,
            ) {
                //Bottom Navigation View
                fragment<HomeFragment, Destination.Home>()
                fragment<ProductListFragment, Destination.Warehouse>()
                fragment<OrderListFragment, Destination.Orders>()
                dialog<MoreFragment, Destination.More>()

                //More Additional Fragments
                fragment<OrganisationUserListFragment, Destination.ManageUsers>()

                //Product
                fragment<ProductDetailFragment, Destination.ProductDetail>()
                dialog<ProductCreateFragment, Destination.CreateProduct>()
                dialog<ProductEditFragment, Destination.EditProduct>()

                //Reports
                fragment<ReportsFragment, Destination.Reports>()

                //Order
                fragment<OrderDetailFragment, Destination.OrderDetail>()
                dialog<OrderCreateFragment, Destination.CreateOrder>()
                dialog<OrderEditFragment, Destination.EditOrder>()
                dialog<OrderAddProductFragment, Destination.OrderAddProduct>()
                dialog<OrderManageDiscountFragment, Destination.OrderManageDiscount>()

                //Warehouse
                fragment<WarehouseFilterFragment, Destination.WarehouseFilters>()

                /*dialog<UpdateProductsStockFragment, Destination.UpdateProductsStock>()*/

                dialog<ProductUpdateStockInFragment, Destination.StockIn>()
                dialog<ProductUpdateStockOutFragment, Destination.StockOut>()

                //Organisation User
                dialog<OrganisationUserManageFragment, Destination.OrganisationManageUser>()
                dialog<OrganisationUserEditFragment, Destination.EditOrganisationUser>()
                dialog<OrganisationUserAssignRoleFragment, Destination.AssignRoleOrganisationUser>()

                dialog<SelectDatePeriodFragment, Destination.SelectDatePeriod>()

                //Organization
                fragment<CreateOrganisationFragment, Destination.CreateOrganisation>()

                fragment<InvitationListAfterSignUpFragment, Destination.InvitationListAfterSignUp>()

                fragment<ProfileFragment, Destination.Profile>()

                fragment<ChangePasswordFragment, Destination.ChangePassword>()
                fragment<ChangeEmailFragment, Destination.ChangeEmail>()

                fragment<InvitationListFragment, Destination.Invitations>()

                fragment<SettingsFragment, Destination.Settings>()

                fragment<OrganisationListFragment, Destination.OrganisationList>()

                fragment<OrganisationManageFragment, Destination.ManageOrganisation>()

                //Authentication
                fragment<AuthenticationFragment, Destination.Authentication>()
                fragment<SignUpFragment, Destination.SignUp>()
                fragment<SignInFragment, Destination.SignIn>()
                fragment<ForgotPasswordFragment, Destination.ForgotPassword>()
                fragment<VerificationOtpFragment, Destination.VerificationOtp>()
                fragment<ResetPasswordFragment, Destination.ResetPassword>()
            }
        }
    }

    private fun setupWithNavController(
        lifecycleOwner: LifecycleOwner,
        navigator: AppNavigator,
        navController: NavController
    ) {
        lifecycleOwner.collectInLifecycle(
            navigator.navigationActions,
            Lifecycle.State.STARTED,
            Dispatchers.Main.immediate
        ) { action ->
            when (action) {
                is NavigationAction.Navigate -> {
                    navController.navigate(
                        route = action.destination,
                    ) {
                        val isAnyMoreDestination =
                            Destination.getMoreDestinations().any { action.destination == it }
                        anim {
                            this.enter =
                                if (isAnyMoreDestination) R.anim.from_bottom else R.anim.from_right

                            this.exit =
                                if (isAnyMoreDestination) R.anim.from_alpha else R.anim.to_left

                            this.popEnter =
                                if (isAnyMoreDestination) R.anim.to_alpha else R.anim.from_left

                            this.popExit =
                                if (isAnyMoreDestination) R.anim.from_alpha else R.anim.to_right
                        }
                        action.navOptions(this)
                    }
                }

                is NavigationAction.NavigateUp -> {
                    try {
                        action.args.forEach { (key, value) ->
                            navController.previousBackStackEntry?.savedStateHandle?.set(key, value)
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
