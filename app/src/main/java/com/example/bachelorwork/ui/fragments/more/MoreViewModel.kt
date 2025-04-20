package com.example.bachelorwork.ui.fragments.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.navigation.AppNavigator
import com.example.bachelorwork.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val navigator: AppNavigator
): ViewModel() {

    fun navigateToManageUsers() {        navigateTo(Destination.ManageUsers)
    }

    /*fun navigateToManageOrganisationRoles() {
        navigateTo(Destination.ManageRoles)
    }*/

    fun navigateToManageOrganisation() {
        navigateTo(Destination.ManageOrganisation)
    }

    private fun navigateTo(destination: Destination) = viewModelScope.launch {
        navigator.navigate(destination) {
            launchSingleTop = true

            popUpTo(Destination.More) {
                inclusive = true
            }
        }
    }

}