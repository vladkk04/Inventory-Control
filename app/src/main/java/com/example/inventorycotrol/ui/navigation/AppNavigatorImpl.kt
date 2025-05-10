package com.example.inventorycotrol.ui.navigation

import androidx.navigation.NavOptionsBuilder
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.domain.manager.DataStoreManager
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow

class AppNavigatorImpl(
    private val dataStoreManager: DataStoreManager,
    private val authDataSource: AuthRemoteDataSource
): AppNavigator {

    private val _navigate = Channel<NavigationAction>()
    override val navigationActions = _navigate.receiveAsFlow()

    override fun startDestination(): Flow<Destination> = flow {
        val isUserNotSelectedOrganisation = dataStoreManager.getPreference(AppConstants.SELECTED_ORGANISATION_ID).firstOrNull().isNullOrEmpty()
        val isUserNew = dataStoreManager.getPreference(AppConstants.JWT_ACCESS_TOKEN_KEY).firstOrNull().isNullOrEmpty()

        if (isUserNew) {
            emit(Destination.Authentication)
        } else if (isUserNotSelectedOrganisation) {
            emit(Destination.InvitationListAfterSignUp)
        } else {
            emit(Destination.Home)
        }
    }.flowOn(Dispatchers.IO)


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