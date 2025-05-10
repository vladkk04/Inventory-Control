package com.example.inventorycotrol.ui.fragments.organisationUsers.manage.email

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrganisationUserInvitationEmailBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.ui.fragments.organisationUsers.manage.OrganisationUserManageViewModel
import com.example.inventorycotrol.ui.model.organisationUser.OrganisationUserManageUiState
import com.example.inventorycotrol.ui.model.organisationUser.invitationEmail.OrganisationUserInvitationEmailFormEvent
import com.example.inventorycotrol.ui.model.organisationUser.invitationEmail.OrganisationUserInvitationEmailFormState
import com.example.inventorycotrol.ui.model.organisationUser.invitationEmail.OrganisationUserInvitationEmailUiState
import com.example.inventorycotrol.ui.snackbar.SnackbarController
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationUserInvitationEmailFragment :
    Fragment(R.layout.fragment_organisation_user_invitation_email) {

    private val binding by viewBinding(FragmentOrganisationUserInvitationEmailBinding::bind)

    private val viewModel: OrganisationUserInvitationEmailViewModel by viewModels()

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
            viewModel.onEvent(OrganisationUserInvitationEmailFormEvent.Invite)
        }
    }

    private fun updateFormState(uiState: OrganisationUserInvitationEmailFormState) {
        binding.textInputEmail.error = uiState.emailError
    }

    private fun setupEditTextChanges() {
        binding.editTextOrganisationUserName.doAfterTextChanged {
            viewModel.onEvent(
                OrganisationUserInvitationEmailFormEvent.OrganisationUserNameChanged(it.toString())
            )
        }
        binding.editTextEmail.doAfterTextChanged {
            viewModel.onEvent(OrganisationUserInvitationEmailFormEvent.EmailChanged(it.toString()))
        }
    }

    private fun updateUiState(uiState: OrganisationUserInvitationEmailUiState) {
        val buttonInviteText =  if (uiState.inviteAsNewUser) getString(R.string.text_invite_user_to_organisation_via_email) else getString(R.string.text_invite)

        binding.buttonInvite.text = buttonInviteText

        if (uiState.isLoading) {
            binding.buttonInvite.showProgress()
        } else {
            binding.buttonInvite.hideProgress(buttonInviteText)
        }
    }

    private fun updateSharedUiState(uiState: OrganisationUserManageUiState) {
        (binding.autoCompleteRole as MaterialAutoCompleteTextView).apply {
            setSimpleItems(OrganisationRole.entries.map { it.name }.toTypedArray())
        }.setOnItemClickListener { _, _, i, _ ->
            viewModel.onEvent(OrganisationUserInvitationEmailFormEvent.RoleChanged(OrganisationRole.entries[i]))
        }
    }
}