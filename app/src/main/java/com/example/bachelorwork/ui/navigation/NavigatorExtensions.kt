package com.example.bachelorwork.ui.navigation

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.updatePadding
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

fun NavigationBarView.setupWithNavController(
    navController: NavController,
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        topLevelRoutes.find { destination.hasRoute(it.route::class) }?.let { dest ->
            menu.findItemByTitle(dest.route::class.simpleName)?.isChecked = true
        }
    }
    setOnItemSelectedListener { item ->
        topLevelRoutes.find { it.route::class.simpleName == item.title }?.takeIf { route ->
            navController.currentDestination?.hasRoute(route.route::class) != true
        }?.let { route ->
            navController.navigate(route.route) {
                restoreState = true
                launchSingleTop = true
            }
            return@setOnItemSelectedListener true
        }
        false
    }
}

fun NavigationView.setupWithNavController(
    navController: NavController
) {
    navController.addOnDestinationChangedListener { _, destination, _ ->
        
    }
    this.setNavigationItemSelectedListener {
        Log.d("debug", "hello")
        true
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