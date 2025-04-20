package com.example.bachelorwork.ui.fragments.auth.resetPassword

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentResetPasswordBinding
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.model.auth.resetPassword.ResetPasswordUiEvent
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private val binding by viewBinding(FragmentResetPasswordBinding::bind)

    private val viewModel by viewModels<ResetPasswordViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)

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