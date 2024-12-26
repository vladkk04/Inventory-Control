package com.example.bachelorwork.ui.fragments.more.manageUsers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ManageUsersViewModel @Inject constructor(
    val navigator: Navigator,
): ViewModel() {

    fun navigateToCreateUser() = viewModelScope.launch {
        navigator.navigate(Destination.CreateNewUser)
    }
}
