package com.example.bachelorwork.ui.navigation

import androidx.navigation.NavOptionsBuilder

sealed class NavigationAction {
    data class Navigate(
        val destination: Destination,
        val navOptions: NavOptionsBuilder.() -> Unit = {},
    ) : NavigationAction()

    data object NavigateUp: NavigationAction()

    data object OpenNavigationDrawer: NavigationAction()

}