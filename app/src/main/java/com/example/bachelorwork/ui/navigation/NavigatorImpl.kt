package com.example.bachelorwork.ui.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class NavigatorImpl(
    override val startDestination: Destination
): Navigator {

    private val _navigate = Channel<NavigationAction>()
    override val navigationActions = _navigate.receiveAsFlow()

    override suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        _navigate.send(NavigationAction.Navigate(destination, navOptions))
    }

    override suspend fun navigateUp(args: Map<String, Any?>) {
        _navigate.send(NavigationAction.NavigateUp(args))
    }

    override suspend fun openNavigationDrawer() {
        _navigate.send(NavigationAction.OpenNavigationDrawer)
    }
}