package com.example.inventorycotrol.ui.fragments.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentAuthenticationBinding
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthenticationFragment: Fragment(R.layout.fragment_authentication) {

    private val binding by viewBinding(FragmentAuthenticationBinding::bind)

    private val viewModel: AuthenticationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAuthButtons()
    }

    private fun setupAuthButtons() {
        binding.buttonLogIn.setOnClickListener {
            viewModel.navigateToSignIn()
        }
        binding.buttonSignUp.setOnClickListener {
            viewModel.navigateToSignUp()
        }
    }

}