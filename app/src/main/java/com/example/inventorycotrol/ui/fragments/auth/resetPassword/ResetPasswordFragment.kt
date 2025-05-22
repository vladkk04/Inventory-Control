package com.example.inventorycotrol.ui.fragments.auth.resetPassword

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentResetPasswordBinding
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.model.auth.resetPassword.ResetPasswordUiEvent
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private val binding by viewBinding(FragmentResetPasswordBinding::bind)

    private val viewModel by viewModels<ResetPasswordViewModel>()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.buttonResetPassword.isEnabled = it
            }
        }

        bindProgressButton(binding.buttonResetPassword)

        collectInLifecycle(viewModel.uiState) {
            if (it.isLoading) {
                binding.buttonResetPassword.showProgress()
            } else {
                binding.buttonResetPassword.hideProgress(R.string.text_reset_password)
            }
        }

        collectInLifecycle(viewModel.uiFormState) {
            binding.textInputPassword.error = it.passwordError
            binding.textInputConfirmPassword.error = it.confirmPasswordError
        }

        binding.editTextPassword.doAfterTextChanged {
            viewModel.onEvent(ResetPasswordUiEvent.PasswordChanged(it.toString()))
        }

        binding.editTextConfirmPassword.doAfterTextChanged {
            viewModel.onEvent(ResetPasswordUiEvent.PasswordConfirmChanged(it.toString()))
        }

        binding.buttonResetPassword.setOnClickListener {
            viewModel.onEvent(ResetPasswordUiEvent.ResetPassword)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AppDialogs.createCancelDialog(requireContext(), onPositiveButtonClick = {
                        viewModel.navigateUp()
                    }).show()
                }
            }
        )
    }
}