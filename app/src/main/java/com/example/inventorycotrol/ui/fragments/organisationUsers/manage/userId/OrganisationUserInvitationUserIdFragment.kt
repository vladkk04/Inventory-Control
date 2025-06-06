package com.example.inventorycotrol.ui.fragments.organisationUsers.manage.userId

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrganisationUserInvitationUserIdBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdFormEvent
import com.example.inventorycotrol.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdFormState
import com.example.inventorycotrol.ui.model.organisationUser.invitationUserId.OrganisationUserInvitationUserIdUiState
import com.example.inventorycotrol.ui.snackbar.SnackbarController
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OrganisationUserInvitationUserIdFragment: Fragment(R.layout.fragment_organisation_user_invitation_user_id) {

    private val binding by viewBinding(FragmentOrganisationUserInvitationUserIdBinding::bind)

    private val viewModel: OrganisationUserInvitationUserIdViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)
        SnackbarController.observeSnackbarEvents(viewLifecycleOwner, binding.root)

        setupEditTextChanges()
        setupInviteButton()
        setupAutoCompleteTextViewUserRole()

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.buttonInvite.isEnabled = it
            }
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
            binding.buttonInvite.isEnabled = it && mainViewModel.isConnected.value
        }
    }

    private fun setupAutoCompleteTextViewUserRole() {
        (binding.autoCompleteRole as MaterialAutoCompleteTextView).apply {
            setSimpleItems(OrganisationRole.entries.map { it.name }.toTypedArray())
        }.setOnItemClickListener { _, _, i, _ ->
            viewModel.onEvent(OrganisationUserInvitationUserIdFormEvent.RoleChanged(OrganisationRole.entries[i].name))
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
}