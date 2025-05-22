package com.example.inventorycotrol.ui.fragments.organisation

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentOrganisationListBinding
import com.example.inventorycotrol.databinding.OrganisationManageItemBinding
import com.example.inventorycotrol.domain.model.organisation.Organisation
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrganisationListFragment : Fragment(R.layout.fragment_organisation_list) {

    private val binding by viewBinding(FragmentOrganisationListBinding::bind)

    private val viewModel: OrganisationListViewModel by viewModels()

    private val adapter = simpleAdapter<Organisation, OrganisationManageItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { organisation ->
            Glide.with(requireContext())
                .load("${AppConstants.BASE_URL_CLOUD_FRONT}${organisation.logoUrl}")
                .placeholder(R.drawable.ic_building)
                .fallback(R.drawable.ic_building)
                .error(R.drawable.ic_building)
                .centerCrop()
                .into(imageViewOrganisationLogo)

            organisationName.text = organisation.name
        }
        listeners {
            root.onClick { item ->
                viewModel.switchOrganisation(item.id)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupRecyclerView()
        setupSwipeRefreshLayout()

        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: OrganisationListUiState) {
        binding.progressBar.isVisible = uiState.isLoading && !uiState.isRefreshing
        binding.swipeRefreshLayout.isRefreshing = uiState.isRefreshing && !uiState.isLoading
        binding.textViewNoOrganisation.isGone = uiState.organisations.isNotEmpty() && !uiState.isLoading
        adapter.submitList(uiState.organisations)
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrganisationListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}