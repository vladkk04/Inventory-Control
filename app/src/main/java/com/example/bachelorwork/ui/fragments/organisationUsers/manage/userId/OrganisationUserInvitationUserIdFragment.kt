package com.example.bachelorwork.ui.fragments.organisationUsers.manage.userId

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrganisationUserInvitationUserIdBinding
import com.example.bachelorwork.domain.model.organisation.OrganisationRole
import com.example.bachelorwork.ui.fragments.organisationUsers.manage.OrganisationUserManageViewModel
import com.example.bachelorwork.ui.model.organisationUser.OrganisationUserManageUiState
import com.example.bachelorwork.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdFormEvent
import com.example.bachelorwork.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdFormState
import com.example.bachelorwork.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdUiState
import com.example.bachelorwork.ui.snackbar.SnackbarController
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationUserInvitationUserIdFragment :
    Fragment(R.layout.fragment_organisation_user_invitation_user_id) {

    private val binding by viewBinding(FragmentOrganisationUserInvitationUserIdBinding::bind)

    private val viewModel: OrganisationUserInvitationUserIdViewModel by viewModels()

    private val sharedViewModel: OrganisationUserManageViewModel by viewModels({ requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        SnackbarController.observeSnackbarEvents(viewLifecycleOwner, binding.root)

        setupEditTextChanges()
        setupInviteButton()

        collectInLifecycle(sharedViewModel.uiState) { uiState ->
            updateSharedUiState(uiState)
        }

        collectInLifecycle(viewModel.uiState) { uiState ->
            updateUiState(uiState)
        }

        collectInLifecycle(viewModel.uiStateForm) { uiState ->
            updateFormState(uiState)
        }

        collectInLifecycle(viewModel.organisationUserName) {
            binding.editTextOrganisationUserName.setText(it)
        }

        collectInLifecycle(viewModel.canInvite) {
            binding.buttonInvite.isEnabled = it
        }
    }

    private fun setupInviteButton() {
        bindProgressButton(binding.buttonInvite)
        binding.buttonInvite.setOnClickListener {
            viewModel.onEvent(OrganisationUserInvitationUserIdFormEvent.Invite)
        }
    }

    private fun updateUiState(uiState: OrganisationUserInvitationUserIdUiState) {
        if (uiState.isLoading) {
            binding.buttonInvite.showProgress()
        } else {
            binding.buttonInvite.hideProgress(R.string.text_invite)
        }
    }

    private fun updateFormState(uiState: OrganisationUserInvitationUserIdFormState) {
        binding.textInputUserId.error = uiState.userIdError
    }

    private fun setupEditTextChanges() {
        binding.editTextOrganisationUserName.doAfterTextChanged {
            viewModel.onEvent(
                OrganisationUserInvitationUserIdFormEvent.OrganisationUserNameChanged(it.toString())
            )
        }
        binding.editTextUserId.doAfterTextChanged {
            viewModel.onEvent(OrganisationUserInvitationUserIdFormEvent.UserIdChanged(it.toString()))
        }
    }

    private fun updateSharedUiState(uiState: OrganisationUserManageUiState) {
        (binding.autoCompleteRole as MaterialAutoCompleteTextView).apply {
            setSimpleItems(OrganisationRole.entries.map { it.name }.toTypedArray())
        }.setOnItemClickListener { _, _, i, _ ->
            viewModel.onEvent(OrganisationUserInvitationUserIdFormEvent.RoleChanged(OrganisationRole.entries[i].name))
        }
    }
}