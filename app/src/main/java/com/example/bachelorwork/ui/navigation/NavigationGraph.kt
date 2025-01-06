package com.example.bachelorwork.ui.navigation

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.fragments.home.HomeFragment
import com.example.bachelorwork.ui.fragments.more.MoreBottomSheetFragment
import com.example.bachelorwork.ui.fragments.more.userList.UserListFragment
import com.example.bachelorwork.ui.fragments.orders.orderList.OrderListFragment
import com.example.bachelorwork.ui.fragments.orders.create.OrderCreateModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.orders.create.addProduct.OrderAddProductModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.orders.detail.OrderDetailFragment
import com.example.bachelorwork.ui.fragments.users.CreateUserModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.warehouse.productCreate.ProductCreateModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.ProductDetailFragment
import com.example.bachelorwork.ui.fragments.warehouse.productEdit.ProductEditModalBottomSheetFragment
import com.example.bachelorwork.ui.fragments.warehouse.productList.ProductListFragment
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
            startDestination = navigator.startDestination
        ) {
            fragment<HomeFragment, Destination.Home>()
            fragment<ProductListFragment, Destination.Warehouse>()
            fragment<ProductDetailFragment, Destination.ProductDetail>()
            fragment<OrderListFragment, Destination.Orders>()

            fragment<UserListFragment, Destination.ManageUsers>()
            fragment<MoreBottomSheetFragment, Destination.More>()

            fragment<OrderDetailFragment, Destination.OrderDetail>()

            dialog<OrderCreateModalBottomSheetFragment, Destination.CreateOrder>()
            dialog<OrderAddProductModalBottomSheetFragment, Destination.OrderAddProduct>()

            dialog<ProductCreateModalBottomSheetFragment, Destination.CreateProduct>()
            dialog<ProductEditModalBottomSheetFragment, Destination.EditProduct>()
            dialog<CreateUserModalBottomSheetFragment, Destination.CreateNewUser>()
            dialog<MoreBottomSheetFragment, Destination.More>()
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
                    navController.navigateUp()
                }

                NavigationAction.OpenNavigationDrawer -> {
                    drawerLayout?.open()
                }
            }
        }
    }
}
