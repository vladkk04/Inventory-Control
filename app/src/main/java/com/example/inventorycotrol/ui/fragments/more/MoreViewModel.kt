package com.example.inventorycotrol.ui.fragments.more

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val navigator: AppNavigator
): ViewModel() {

    fun navigateToManageUsers() {
        navigateTo(Destination.ManageOrganisationUsers)
    }

    fun navigateToManageOrganisation() {
        navigateTo(Destination.ManageOrganisation)
    }

    fun navigateToOrganisationSettings() {
        navigateTo(Destination.ManageOrganisationSettings)
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