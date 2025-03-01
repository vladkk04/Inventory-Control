package com.example.bachelorwork.ui.navigation

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

fun NavigationBarView.setupWithNavController(
    navController: NavController,
    drawerLayout: DrawerLayout? = null,
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        this.visibility = if (!Destination.getTopLevelDestinations()
                .any { destination.hasRoute(it::class) }
        ) View.GONE else View.VISIBLE

        Destination.getTopLevelDestinations().find { destination.hasRoute(it::class) }
            ?.let { dest -> findItemByTitle(menu, dest::class.simpleName)?.isChecked = true }
    }

    var isDialogTopDestination = false

    setOnItemSelectedListener { item ->
        Destination.getTopLevelDestinations().find { it::class.simpleName == item.title }
            ?.let { selectedRoute ->

                if (navController.currentDestination?.hasRoute(selectedRoute::class) == true) {
                    return@setOnItemSelectedListener true
                }

                findItemByTitle(menu, selectedRoute::class.simpleName)?.isChecked = true

                val builder = NavOptions.Builder().apply {
                    setLaunchSingleTop(true)

                    if (selectedRoute == Destination.More) {
                        isDialogTopDestination = true
                        navController.currentBackStackEntry?.destination?.id?.let {
                            setPopUpTo(it, false)
                        }
                    } else {
                        if (isDialogTopDestination) {
                            navController.navigateUp()
                            isDialogTopDestination = false
                        }

                        setRestoreState(true)
                        setPopUpTo(
                            navController.graph.findStartDestination().id,
                            inclusive = false,
                            saveState = true
                        )
                    }
                }.build()

                navController.navigate(selectedRoute, builder)
            }
        false
    }
}

fun NavigationView.setupWithNavController(
    navController: NavController,
    drawerLayout: DrawerLayout,
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        drawerLayout.setDrawerLockMode(shouldLockDrawer(destination))
    }
    this.setNavigationItemSelectedListener {
        true
    }
}

private fun shouldLockDrawer(
    destination: NavDestination,
): Int {
    return if (!Destination.getTopLevelDestinations()
            .any { destination.hasRoute(it::class) }
    ) DrawerLayout.LOCK_MODE_LOCKED_CLOSED else DrawerLayout.LOCK_MODE_UNLOCKED
}


private fun findItemByTitle(menu: Menu, title: String?): MenuItem? {
    for (i in 0 until menu.size()) {
        val item = menu.getItem(i)
        if (item.title == title) {
            return item
        }
    }
    return null
}


private fun hideNavigationBarWithAnimation(
    navBar: NavigationBarView,
    fragmentContainer: FragmentContainerView
) {
    fragmentContainer.updatePadding(bottom = 0)

    navBar.animate()
        .translationY(navBar.height.toFloat())
        .alpha(0f)
        .setDuration(200)
        .start()
}

private fun showNavigationBarWithAnimation(
    navBar: NavigationBarView,
    fragmentContainer: FragmentContainerView
) {
    fragmentContainer.updatePadding(bottom = 240)

    navBar.animate()
        .translationY(0f)
        .alpha(1f)
        .setDuration(200)
        .start()
}