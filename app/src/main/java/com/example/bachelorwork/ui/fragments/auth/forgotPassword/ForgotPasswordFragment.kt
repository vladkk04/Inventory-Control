package com.example.bachelorwork.ui.fragments.auth.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentForgotPasswordBinding
import com.example.bachelorwork.ui.model.auth.forgotPassword.ForgotPasswordUiEvent
import com.example.bachelorwork.ui.model.auth.forgotPassword.ForgotPasswordUiFormState
import com.example.bachelorwork.ui.model.auth.forgotPassword.ForgotPasswordUiState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment: Fragment(R.layout.fragment_forgot_password) {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        setupEditTextListeners()
        setupSendButton()
        setupBackButton()

        collectInLifecycle(viewModel.uiState) {
            updateUiState(it)
        }

        collectInLifecycle(viewModel.uiStateForm) { state ->
            updateUiFormState(state)
        }
    }

    private fun setupEditTextListeners() {
        binding.editTextEmail.doAfterTextChanged {
            viewModel.onEvent(ForgotPasswordUiEvent.EmailChanged(it.toString()))
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            viewModel.onEvent(ForgotPasswordUiEvent.NavigateBack)
        }
    }

    private fun setupSendButton() {
        bindProgressButton(binding.buttonSend)
        binding.buttonSend.setOnClickListener {
            viewModel.onEvent(ForgotPasswordUiEvent.Send)
        }
    }

    private fun updateUiState(uiState: ForgotPasswordUiState) {
        if (uiState.isLoading) {
            binding.buttonSend.showProgress()
        } else {
            binding.buttonSend.hideProgress(R.string.text_send)
        }
    }

    private fun updateUiFormState(uiState: ForgotPasswordUiFormState) {
        binding.textInputEmail.error = uiState.emailError
    }
}