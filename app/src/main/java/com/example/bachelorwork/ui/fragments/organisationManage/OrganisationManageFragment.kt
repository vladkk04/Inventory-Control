package com.example.bachelorwork.ui.fragments.organisationManage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bachelorwork.R
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.databinding.FragmentOrganisationManageBinding
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationManageFragment : Fragment(R.layout.fragment_organisation_manage) {

    private val binding by viewBinding(FragmentOrganisationManageBinding::bind)

    private val viewModel: OrganisationManageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupDeleteOrganisationButton()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: OrganisationManageUiState) {

        Glide.with(requireContext())
            .load("${AppConstants.BASE_URL}${uiState.organisation?.logoUrl}")
            .error(R.drawable.ic_building)
            .into(binding.imageViewAvatar)

        binding.textViewOrganisationName.text = uiState.organisation?.name
        binding.textViewOrganisationId.text = uiState.organisation?.id
        binding.textViewOrganisationOwner.text = uiState.organisation?.createdBy
        binding.textViewNoAbout.visibility = if (uiState.organisation?.description.isNullOrEmpty()) View.VISIBLE else View.GONE
        binding.textViewOrganisationDescriptionValue.text = uiState.organisation?.description
        binding.textViewOrganisationDescriptionValue.visibility = if (uiState.organisation?.description.isNullOrEmpty()) View.GONE else View.VISIBLE

        binding.buttonDeleteOrganisation.isEnabled = uiState.organisation != null
        binding.buttonDeleteOrganisation.visibility = if (uiState.organisation != null) View.VISIBLE else View.GONE

    }

    private fun setupDeleteOrganisationButton() {
        binding.buttonDeleteOrganisation.setOnClickListener {
            viewModel.deleteOrganisation()
        }
    }
}