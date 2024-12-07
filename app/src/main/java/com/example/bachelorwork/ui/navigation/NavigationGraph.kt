package com.example.bachelorwork.ui.navigation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.createGraph
import androidx.navigation.fragment.dialog
import androidx.navigation.fragment.fragment
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.fragments.home.HomeFragment
import com.example.bachelorwork.ui.fragments.profile.ProfileFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.ProductDetailFragment
import com.example.bachelorwork.ui.fragments.warehouse.productList.ProductListFragment
import com.example.bachelorwork.ui.fragments.warehouse.productManage.ProductManageModalBottomSheetFragment
import kotlinx.coroutines.Dispatchers


class NavigationGraph(
    lifecycleOwner: LifecycleOwner,
    private val navigator: Navigator,
    private val navController: NavController,
) {
    init {
        setupWithNavController(lifecycleOwner, navigator, navController)
        createGraph()
    }

    private fun createGraph() {
        navController.graph = navController.createGraph(
            startDestination = navigator.startDestination
        ) {
            fragment<HomeFragment, Destination.Home> {

            }
            fragment<ProductListFragment, Destination.Warehouse> {

            }
            fragment<ProfileFragment, Destination.Profile> {

            }
            fragment<ProductDetailFragment, Destination.ProductDetail> {

            }
            dialog<ProductManageModalBottomSheetFragment, Destination.ProductManage> {

            }
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
                        anim {
                            popEnter =
                                androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
                            popExit =
                                androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
                            enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
                            exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                        }
                    }
                }
                is NavigationAction.NavigateUp -> navController.navigateUp()
            }
        }
    }
}
