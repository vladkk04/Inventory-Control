package com.example.inventorycotrol.ui.fragments.auth.signIn

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentSignInBinding
import com.example.inventorycotrol.ui.model.auth.signIn.SignInUiEvent
import com.example.inventorycotrol.ui.model.auth.signIn.SignInUiFormState
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupBackButton()
        setupOnInputsEditTextChange()
        setupForgotPasswordButton()
        setupSignInButton()
        setupSignUpButton()

        collectInLifecycle(viewModel.uiState) {
            if (it.isLoading) {
                binding.buttonSignIn.showProgress()
            } else {
                binding.buttonSignIn.hideProgress(R.string.text_sign_in)
            }
        }

        collectInLifecycle(viewModel.uiStateForm, Lifecycle.State.STARTED) {
            setupUiFormState(it)
        }

    }

    private fun setupUiFormState(uiFormState: SignInUiFormState) {
        binding.textInputEmail.error = uiFormState.emailError
        binding.textInputPassword.error = uiFormState.passwordError
    }

    private fun setupSignInButton() {
        bindProgressButton(binding.buttonSignIn)
        binding.buttonSignIn.setOnClickListener {
            viewModel.onEvent(SignInUiEvent.SignIn)
        }
    }

    private fun setupSignUpButton() {
        binding.textSignUp.setOnClickListener {
            viewModel.onEvent(SignInUiEvent.NavigateToSignUp)
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            viewModel.onEvent(SignInUiEvent.NavigateBack)
        }
    }

    private fun setupForgotPasswordButton() {
        binding.textViewForgotPassword.setOnClickListener {
            viewModel.onEvent(SignInUiEvent.ForgotPassword)
        }
    }

    private fun setupOnInputsEditTextChange() {
        setupTextChangeListener(binding.editTextEmail, SignInUiEvent::EmailChanged)
        setupTextChangeListener(binding.editTextPassword, SignInUiEvent::PasswordChanged)
    }

    private fun setupTextChangeListener(
        editText: EditText,
        event: (String) -> SignInUiEvent,
    ) {
        editText.doAfterTextChanged {
            viewModel.onEvent(event.invoke(it.toString()))
        }
    }
}