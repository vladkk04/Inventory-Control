package com.example.bachelorwork.ui.navigation

import android.view.Menu
import android.view.MenuItem
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.google.android.material.navigation.NavigationBarView

fun NavigationBarView.setupWithNavController(
    navController: NavController,
    fragmentContainer: FragmentContainerView
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        val isTopLevelRoute = topLevelRoutes.any { destination.hasRoute(it.route::class) }

        if (!isTopLevelRoute) {
            hideNavigationBarWithAnimation(this, fragmentContainer)
        } else {
            showNavigationBarWithAnimation(this, fragmentContainer)
        }

        topLevelRoutes.find { destination.hasRoute(it.route::class) }?.let { dest ->
            menu.findItemByTitle(dest.route::class.simpleName)?.isChecked = true
        }
    }

    setOnItemSelectedListener { item ->
        topLevelRoutes.find { it.route::class.simpleName == item.title }?.takeIf { route ->
            navController.currentDestination?.hasRoute(route.route::class) != true
        }?.let { route ->
            navController.navigate(route.route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            return@setOnItemSelectedListener true
        }
        false
    }
}

private fun Menu.findItemByTitle(title: String?): MenuItem? {
    for (i in 0 until size()) {
        val item = getItem(i)
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
)
{
    fragmentContainer.updatePadding(bottom = 240)

    navBar.animate()
        .translationY(0f)
        .alpha(1f)
        .setDuration(200)
        .start()
}