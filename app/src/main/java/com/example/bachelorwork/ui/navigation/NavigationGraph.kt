package com.example.bachelorwork.ui.navigation

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.bachelorwork.ui.fragments.home.HomeFragment
import com.example.bachelorwork.ui.fragments.more.MoreBottomSheetFragment
import com.example.bachelorwork.ui.fragments.orders.create.OrderCreateModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.orders.create.manage.addProduct.OrderAddProductModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.orders.create.manage.discount.OrderManageDiscountModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.orders.create.manage.editAddedProduct.OrderEditAddedProductModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.orders.detail.OrderDetailFragment
import com.example.bachelorwork.ui.fragments.orders.orderList.OrderListFragment
import com.example.bachelorwork.ui.fragments.updateProductStock.UpdateProductsStockModalBottomSheet
import com.example.bachelorwork.ui.fragments.users.CreateUserModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.users.UserListFragment
import com.example.bachelorwork.ui.fragments.warehouse.productCreate.ProductCreateModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.ProductDetailFragment
import com.example.bachelorwork.ui.fragments.warehouse.productEdit.ProductEditModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.warehouse.productList.ProductListFragment
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import kotlinx.coroutines.Dispatchers


class NavigationGraph(
    lifecycleOwner: LifecycleOwner,
    private val navigator: Navigator,
    private val navController: NavController,
) {
    private var appBarConfiguration: AppBarConfiguration? = null
    private var drawerLayout: DrawerLayout? = null

    constructor(
        lifecycleOwner: LifecycleOwner,
        navigator: Navigator,
        navController: NavController,
        appBarConfiguration: AppBarConfiguration
    ) : this(lifecycleOwner, navigator, navController) {
        this.appBarConfiguration = appBarConfiguration
    }

    constructor(
        lifecycleOwner: LifecycleOwner,
        navigator: Navigator,
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
            fragment<HomeFragment, Destination.Home>()
            fragment<ProductListFragment, Destination.Warehouse>()
            fragment<OrderListFragment, Destination.Orders>()
            dialog<MoreBottomSheetFragment, Destination.More>()

            fragment<UserListFragment, Destination.ManageUsers>()

            fragment<ProductDetailFragment, Destination.ProductDetail>()
            fragment<OrderDetailFragment, Destination.OrderDetail>()

            dialog<OrderCreateModalBottomSheetFragment, Destination.CreateOrder>()
            dialog<OrderAddProductModalBottomSheetFragment, Destination.OrderAddProduct>()
            dialog<OrderEditAddedProductModalBottomSheetFragment, Destination.OrderEditAddedProduct>()
            dialog<OrderManageDiscountModalBottomSheetFragment, Destination.OrderManageDiscount>()

            dialog<UpdateProductsStockModalBottomSheet, Destination.UpdateProductsStock>()

            dialog<ProductCreateModalBottomSheetFragment, Destination.CreateProduct>()
            dialog<ProductEditModalBottomSheetFragment, Destination.EditProduct>()
            dialog<CreateUserModalBottomSheetFragment, Destination.CreateNewUser>()
        }
    }

    private fun setupWithNavController(
        lifecycleOwner: LifecycleOwner,
        navigator: Navigator,
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
