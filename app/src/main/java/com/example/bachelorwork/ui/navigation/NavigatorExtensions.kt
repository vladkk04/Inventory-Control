package com.example.bachelorwork.ui.navigation

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavOptions
import com.example.bachelorwork.R
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

fun NavigationBarView.setupWithNavController(
    navController: NavController,
    drawerLayout: DrawerLayout? = null,
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        if (!Destination.getTopLevelDestinations().any { destination.hasRoute(it::class) }) {
            animate().translationY(height.toFloat())
                .setDuration(80)
                .setInterpolator(AccelerateInterpolator())
                .withEndAction { visibility = View.GONE }
        } else {
            visibility = View.VISIBLE
            animate().translationY(0f)
                .setDuration(80)
                .setInterpolator(DecelerateInterpolator())
        }

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
                            Destination.Home,
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

        if (destination.hasRoute(Destination.Home::class)) {
            this.setCheckedItem(R.id.home)
        }

        drawerLayout.setDrawerLockMode(shouldLockDrawer(destination))
    }


    this.setNavigationItemSelectedListener { item ->
        Destination.getDrawerDestinations().find { it::class.simpleName == item.title }
            ?.let { destination ->

                if (navController.currentDestination?.hasRoute(destination::class) == true) {
                    drawerLayout.close()
                    return@setNavigationItemSelectedListener true
                }

                navController.navigate(destination) {
                    launchSingleTop = true
                    popUpTo<Destination.Home> {
                        inclusive = false
                        saveState = false
                    }
                }

                drawerLayout.close()
            }

        true
    }
}

private fun shouldLockDrawer(
    destination: NavDestination,
): Int {
    return if (Destination.getDrawerDestinations().any { destination.hasRoute(it::class) }) {
        DrawerLayout.LOCK_MODE_UNLOCKED
    } else if (Destination.getTopLevelDestinations().any { destination.hasRoute(it::class) }) {
        DrawerLayout.LOCK_MODE_UNLOCKED
    } else {
        DrawerLayout.LOCK_MODE_LOCKED_CLOSED
    }
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
