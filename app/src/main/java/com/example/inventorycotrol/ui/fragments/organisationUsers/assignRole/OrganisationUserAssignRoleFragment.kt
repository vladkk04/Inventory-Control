package com.example.inventorycotrol.ui.fragments.organisationUsers.assignRole

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrganisationUserAssignRoleBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.example.inventorycotrol.util.namesTyped
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationUserAssignRoleFragment : BaseBottomSheetDialogFragment<FragmentOrganisationUserAssignRoleBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrganisationUserAssignRoleBinding
        get() = FragmentOrganisationUserAssignRoleBinding::inflate

    private val viewModel: OrganisationUserAssignRoleViewModel by viewModels()

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    override fun setupViews() {
        super.setupViews()
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupAutoCompleteRole()
        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }

        bindProgressButton(binding.buttonSave)

        binding.autoCompleteRole.setOnItemClickListener { _, _, position, _ ->
            viewModel.changeOrganisationRole(OrganisationRole.entries[position])
        }

        binding.autoCompleteRole.setText(viewModel.organisationRole.value.name, false)

        binding.buttonSave.setOnClickListener {
            viewModel.assignRole()
        }
    }

    private fun setupAutoCompleteRole() {
        (binding.autoCompleteRole as? MaterialAutoCompleteTextView)?.setSimpleItems(
            OrganisationRole.entries.namesTyped()
        )
    }

    private fun updateUiState(uiState: OrganisationUserAssignRoleUiState) {
        if (uiState.isLoading) {
            binding.buttonSave.showProgress()
        } else {
            binding.buttonSave.hideProgress(R.string.text_assign_role)
        }
    }
}