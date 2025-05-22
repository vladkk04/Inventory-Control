package com.example.inventorycotrol.ui.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface AppNavigator {
    val navigationActions: Flow<NavigationAction>

    fun startDestination(): Flow<Destination>

    suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit = { }
    )

    suspend fun navigateUp(args: Map<String, Any?> = emptyMap())

    suspend fun openNavigationDrawer()

}