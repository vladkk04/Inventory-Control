package com.example.inventorycotrol.ui.fragments.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventorycotrol.domain.repository.remote.AuthRemoteDataSource
import com.example.inventorycotrol.ui.navigation.AppNavigator
import com.example.inventorycotrol.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRemoteDataSource,
    private val navigator: AppNavigator,
) : ViewModel() {

    fun navigateToSignIn() = viewModelScope.launch {
        navigator.navigate(Destination.SignIn)
    }

    fun navigateToSignUp() = viewModelScope.launch {
        navigator.navigate(Destination.SignUp)
    }
}