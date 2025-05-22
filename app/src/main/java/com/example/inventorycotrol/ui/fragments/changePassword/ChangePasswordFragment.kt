package com.example.inventorycotrol.ui.fragments.changePassword

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentChangePasswordBinding
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private val binding by viewBinding(FragmentChangePasswordBinding::bind)

    private val viewModel: ChangePasswordViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupToolbar()
        setupEditText()
        setupButtonChangePassword()

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.buttonChangePassword.isEnabled = it
            }
        }
        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
        collectInLifecycle(viewModel.uiFormState) { state ->
            binding.buttonChangePassword.isEnabled =
                state.newPasswordError == null && state.confirmPasswordError == null && mainViewModel.isConnected.value
        }
    }

    private fun setupEditText() {
        binding.editTextPassword.doAfterTextChanged {
            viewModel.onEvent(ChangePasswordUiEvent.NewPassword(it.toString()))
        }
        binding.editTextConfirmPassword.doAfterTextChanged {
            viewModel.onEvent(ChangePasswordUiEvent.ConfirmPassword(it.toString()))
        }
    }

    private fun setupButtonChangePassword() {
        bindProgressButton(binding.buttonChangePassword)
        binding.buttonChangePassword.setOnClickListener {
            viewModel.onEvent(ChangePasswordUiEvent.ChangePassword)
        }
    }



    private fun updateUiState(uiState: ChangePasswordUiState) {
        if (uiState.isLoading) {
            binding.buttonChangePassword.showProgress()
        } else {
            binding.buttonChangePassword.hideProgress(R.string.text_change_password)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}