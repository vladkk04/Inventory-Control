package com.example.inventorycotrol.ui.fragments.organisationManage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentOrganisationManageBinding
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationManageFragment : Fragment() {

    private var _binding: FragmentOrganisationManageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrganisationManageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrganisationManageBinding.inflate(inflater, container, false)
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupDeleteOrganisationButton()
        setupToolbar()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
        return binding.root
    }

    private fun updateUiState(uiState: OrganisationManageUiState) {

        uiState.organisation?.logoUrl?.let {
            Glide.with(requireContext())
                .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it}")
                .error(R.drawable.ic_organisation_building)
                .into(binding.imageViewAvatar)
        }

        binding.textViewOrganisationName.text = uiState.organisation?.name
        binding.textViewOrganisationId.text = getString(R.string.text_your_id, uiState.organisation?.id)
        //binding.textViewOrganisationOwner.text = uiState.organisation?.createdBy
        binding.textViewNoAbout.visibility = if (uiState.organisation?.description.isNullOrEmpty()) View.VISIBLE else View.GONE
        binding.textViewOrganisationDescriptionValue.text = uiState.organisation?.description
        binding.textViewOrganisationDescriptionValue.visibility = if (uiState.organisation?.description.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE

        binding.buttonDeleteOrganisation.isEnabled = uiState.organisation != null
        binding.buttonDeleteOrganisation.visibility = if (uiState.organisation != null) View.VISIBLE else View.GONE

    }

    private fun setupDeleteOrganisationButton() {
        binding.buttonDeleteOrganisation.setOnClickListener {
            viewModel.deleteOrganisation()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.edit -> {
                    viewModel.navigateEditOrganisation()
                    true
                }
                else -> false
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}