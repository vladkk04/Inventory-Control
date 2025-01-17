package com.example.bachelorwork.ui.fragments.users

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.bachelorwork.databinding.FragmentModalBottomSheetUserManageBinding
import com.example.bachelorwork.domain.model.user.Role
import com.example.bachelorwork.ui.model.user.CreateUserUiState
import com.example.bachelorwork.ui.model.user.UserManageFormEvent
import com.example.bachelorwork.ui.model.user.UserManageFormState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.util.namesTyped
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserModalBottomSheetFragment: BottomSheetDialogFragment() {
    private var _binding: FragmentModalBottomSheetUserManageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateUserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModalBottomSheetUserManageBinding.inflate(inflater, container, false)
        setupPasswordInputLayout()
        setupAutoCompleteTextViewUserRole()
        setupButtonCreateUser()
        setupTextInputFieldsOnTextChanged()

        collectInLifecycle(viewModel.uiState) {
            setupUiState(it)
        }

        collectInLifecycle(viewModel.uiStateForm) {
            setupTextInputFieldsError(it)
        }

        //SnackbarController.observeSnackbarEvents(viewLifecycleOwner, binding.root, binding.editTextPassword)

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupUiState(uiState: CreateUserUiState) {
        binding.editTextPassword.setText(uiState.randomPassword)
        (binding.autoCompleteTextViewRole as MaterialAutoCompleteTextView).setText(uiState.role.name, false)
    }

    private fun setupTextInputFieldsError(uiStateFormEvent: UserManageFormState) {
        binding.textInputLayoutFullName.error = uiStateFormEvent.fullNameError
        binding.textInputLayoutEmail.error = uiStateFormEvent.emailError
        binding.textInputLayoutPassword.error = uiStateFormEvent.passwordError
    }

    private fun setupAutoCompleteTextViewUserRole() {
        (binding.autoCompleteTextViewRole as MaterialAutoCompleteTextView).setSimpleItems(Role.entries.namesTyped())
    }

    private fun setupTextInputFieldsOnTextChanged() {
        binding.editTextFullName.doAfterTextChanged {
            viewModel.onEvent(UserManageFormEvent.FullNameChanged(it.toString()))
        }
        binding.editTextEmail.doAfterTextChanged {
            viewModel.onEvent(UserManageFormEvent.EmailChanged(it.toString()))
        }
        binding.editTextPassword.doAfterTextChanged {
            viewModel.onEvent(UserManageFormEvent.PasswordChanged(it.toString()))
        }
    }

    private fun setupPasswordInputLayout() {
        binding.textInputLayoutPassword.setEndIconOnClickListener {
            viewModel.generatePassword()
        }
    }

    private fun setupButtonCreateUser() {
        binding.buttonCreateUser.setOnClickListener {
            viewModel.createUser()
        }
    }

}