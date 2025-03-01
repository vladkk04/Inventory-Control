package com.example.bachelorwork.ui.navigation

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.bachelorwork.ui.fragments.auth.AuthenticationFragment
import com.example.bachelorwork.ui.fragments.auth.signIn.SignInFragment
import com.example.bachelorwork.ui.fragments.auth.signUp.SignUpFragment
import com.example.bachelorwork.ui.fragments.home.HomeFragment
import com.example.bachelorwork.ui.fragments.more.MoreFragment
import com.example.bachelorwork.ui.fragments.orders.detail.OrderDetailFragment
import com.example.bachelorwork.ui.fragments.orders.manage.create.OrderCreateFragment
import com.example.bachelorwork.ui.fragments.orders.manage.discount.OrderManageDiscountFragment
import com.example.bachelorwork.ui.fragments.orders.manage.edit.OrderEditFragment
import com.example.bachelorwork.ui.fragments.orders.manage.manageProduct.OrderAddProductFragment
import com.example.bachelorwork.ui.fragments.orders.manage.manageProduct.OrderEditAddedProductFragment
import com.example.bachelorwork.ui.fragments.orders.orderList.OrderListFragment
import com.example.bachelorwork.ui.fragments.updateProductStock.UpdateProductsStockFragment
import com.example.bachelorwork.ui.fragments.users.CreateUserFragment
import com.example.bachelorwork.ui.fragments.users.UserListFragment
import com.example.bachelorwork.ui.fragments.warehouse.filters.WarehouseFilterFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.ProductDetailFragment
import com.example.bachelorwork.ui.fragments.warehouse.productList.ProductListFragment
import com.example.bachelorwork.ui.fragments.warehouse.productManage.productCreate.ProductCreateFragment
import com.example.bachelorwork.ui.fragments.warehouse.productManage.productEdit.ProductEditFragment
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import kotlinx.coroutines.Dispatchers


class AppNavigationGraph(
    lifecycleOwner: LifecycleOwner,
    private val navigator: AppNavigator,
    private val navController: NavController,
) {
    private var appBarConfiguration: AppBarConfiguration? = null
    private var drawerLayout: DrawerLayout? = null

    constructor(
        lifecycleOwner: LifecycleOwner,
        navigator: AppNavigator,
        navController: NavController,
        drawerLayout: DrawerLayout
    ) : this(lifecycleOwner, navigator, navController) {
        this.drawerLayout = drawerLayout
    }

    init {
        createGraph()
        setupWithNavController(lifecycleOwner, navigator, navController)
    }

    private fun createGraph() {
        navController.graph = navController.createGraph(
            startDestination = navigator.startDestination,
        ) {
            //Bottom Navigation View
            fragment<HomeFragment, Destination.Home>()
            fragment<ProductListFragment, Destination.Warehouse>()
            fragment<OrderListFragment, Destination.Orders>()
            dialog<MoreFragment, Destination.More>()

            //More Additional Fragments
            fragment<UserListFragment, Destination.ManageUsers>()

            //Product
            fragment<ProductDetailFragment, Destination.ProductDetail>()
            dialog<ProductCreateFragment, Destination.CreateProduct>()
            dialog<ProductEditFragment, Destination.EditProduct>()

            //Order
            fragment<OrderDetailFragment, Destination.OrderDetail>()
            dialog<OrderCreateFragment, Destination.CreateOrder>()
            dialog<OrderEditFragment, Destination.EditOrder>()
            dialog<OrderAddProductFragment, Destination.OrderAddProduct>()
            dialog<OrderManageDiscountFragment, Destination.OrderManageDiscount>()
            dialog<OrderEditAddedProductFragment, Destination.OrderEditAddedProduct>()

            //Warehouse
            fragment<WarehouseFilterFragment, Destination.WarehouseFilters>()

            dialog<UpdateProductsStockFragment, Destination.UpdateProductsStock>()

            //User
            dialog<CreateUserFragment, Destination.CreateNewUser>()

            //Authentication
            fragment<AuthenticationFragment, Destination.Authentication>()
            fragment<SignUpFragment, Destination.SignUp>()
            fragment<SignInFragment, Destination.SignIn>()
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
