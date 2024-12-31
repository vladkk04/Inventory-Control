package com.example.bachelorwork.ui.navigation

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
        Destination.getTopLevelDestinations().find { destination.hasRoute(it::class) }
            ?.let { dest ->
                menu.findItemByTitle(dest::class.simpleName)?.isChecked = true
            }
    }
    setOnItemSelectedListener { item ->
        Destination.getTopLevelDestinations().find { it::class.simpleName == item.title }
            ?.takeIf { dest ->
                navController.currentDestination?.hasRoute(dest::class) != true
            }?.let { destination ->
            navController.navigate(destination) {
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
) {
    fragmentContainer.updatePadding(bottom = 240)

    navBar.animate()
        .translationY(0f)
        .alpha(1f)
        .setDuration(200)
        .start()
}