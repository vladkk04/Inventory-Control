package com.example.inventorycotrol.ui.fragments.auth.signUp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentSignUpBinding
import com.example.inventorycotrol.ui.model.auth.signUp.SignUpUiEvent
import com.example.inventorycotrol.ui.model.auth.signUp.SignUpUiFormState
import com.example.inventorycotrol.ui.model.auth.signUp.SignUpUiState
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton()
        setupSignUpButton()
        setupSignInButton()
        setupOnInputsEditTextChange()

        collectInLifecycle(viewModel.uiState) {
            updateUiState(it)
        }

        collectInLifecycle(viewModel.uiFormState) {
            updateFormStateUI(it)
        }
    }

    private fun updateUiState(uiState: SignUpUiState) {
        if(uiState.isLoading) {
            binding.buttonSignUp.showProgress()
        } else {
            binding.buttonSignUp.hideProgress(R.string.text_sign_up)
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            viewModel.onEvent(SignUpUiEvent.NavigateBack)
        }
    }

    private fun setupSignUpButton() {
        binding.buttonSignUp.setOnClickListener {
            viewModel.onEvent(SignUpUiEvent.SignUp)
        }
    }

    private fun setupSignInButton() {
        bindProgressButton(binding.buttonSignUp)
        binding.textSignIn.setOnClickListener {
            viewModel.onEvent(SignUpUiEvent.NavigateToSignIn)
        }
    }

    private fun setupOnInputsEditTextChange() {
        setupTextChangeListener(binding.editTextFullName, SignUpUiEvent::FullNameChanged)
        setupTextChangeListener(binding.editTextEmail, SignUpUiEvent::EmailChanged)
        setupTextChangeListener(binding.editTextPassword, SignUpUiEvent::PasswordChanged)
        setupTextChangeListener(binding.editTextConfirmPassword, SignUpUiEvent::PasswordConfirmChanged)
    }

    private fun updateFormStateUI(uiFormState: SignUpUiFormState) {
        binding.textInputFullName.error = uiFormState.fullNameError
        binding.textInputEmail.error = uiFormState.emailError
        binding.textInputPassword.error = uiFormState.passwordError
        binding.textInputConfirmPassword.error = uiFormState.confirmPasswordError
    }


    private fun setupTextChangeListener(
        editText: EditText,
        event: (String) -> SignUpUiEvent,
    ) {
        editText.doAfterTextChanged {
            viewModel.onEvent(event.invoke(it.toString()))
        }
    }
}