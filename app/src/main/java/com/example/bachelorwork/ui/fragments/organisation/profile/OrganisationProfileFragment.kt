package com.example.bachelorwork.ui.fragments.organisation.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrganisationManageBinding
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrganisationProfileFragment: Fragment(R.layout.fragment_organisation_manage) {

    private val binding by viewBinding(FragmentOrganisationManageBinding::bind)

    private val viewModel: OrganisationProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectInLifecycle(viewModel.uiState) {
            updateUiState(it)
        }
    }

    private fun updateUiState(uiState: OrganisationProfileUiState) {

    }


}