package com.example.bachelorwork.ui.fragments.auth.signUp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentSignUpBinding
import com.example.bachelorwork.ui.model.auth.SignUpUiEvent
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        InsetHandler.adaptToEdgeWithPadding(binding.root)
        setupBackButton()
        setupSignUpButton()
        setupOnInputsEditTextChange()
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

    private fun setupOnInputsEditTextChange() {
        setupTextChangeListener(binding.editTextCompanyName, SignUpUiEvent::CompanyNameChanged)
        setupTextChangeListener(binding.editTextFullName, SignUpUiEvent::FullNameChanged)
        setupTextChangeListener(binding.editTextEmail, SignUpUiEvent::EmailChanged)
        setupTextChangeListener(binding.editTextPassword, SignUpUiEvent::PasswordChanged)
        setupTextChangeListener(binding.editTextConfirmPassword, SignUpUiEvent::PasswordConfirmChanged)
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