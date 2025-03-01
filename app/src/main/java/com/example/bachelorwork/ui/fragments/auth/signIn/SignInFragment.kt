package com.example.bachelorwork.ui.fragments.auth.signIn

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentSignInBinding
import com.example.bachelorwork.ui.model.auth.SignInUiEvent
import com.example.bachelorwork.ui.model.auth.SignInUiFormState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_sign_in) {

    private val binding by viewBinding(FragmentSignInBinding::bind)

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupBackButton()
        setupOnInputsEditTextChange()
        setupSignInButton()

        collectInLifecycle(viewModel.uiStateForm, Lifecycle.State.STARTED) {
            setupUiFormState(it)
        }

    }

    private fun setupUiFormState(uiFormState: SignInUiFormState) {
        binding.textInputEmail.error = uiFormState.emailError
        binding.textInputPassword.error = uiFormState.passwordError
    }

    private fun setupSignInButton() {
        binding.buttonSignIn.setOnClickListener {
            viewModel.onEvent(SignInUiEvent.SignIn)
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            viewModel.onEvent(SignInUiEvent.NavigateBack)
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