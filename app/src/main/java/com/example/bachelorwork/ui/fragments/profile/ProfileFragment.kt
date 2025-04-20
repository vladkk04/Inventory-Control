package com.example.bachelorwork.ui.fragments.profile
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProfileBinding
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupButtons()
        setupToolbar()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }
    
    private fun updateUiState(uiState: ProfileUiState) {
        
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateUp()
        }
    }

    private fun setupButtons() {
        binding.buttonSignOut.setOnClickListener {
            AppDialogs.createSignOutDialog(requireContext()) {
                viewModel.signOut()
            }.show()
        }

        binding.buttonChangeEmail.setOnClickListener {
            viewModel.navigateToChangeEmail()
        }

        binding.buttonChangePassword.setOnClickListener {
            viewModel.navigateToChangePassword()
        }
    }
}