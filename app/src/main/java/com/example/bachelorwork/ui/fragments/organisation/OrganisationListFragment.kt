package com.example.bachelorwork.ui.fragments.organisation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.databinding.FragmentOrganisationListBinding
import com.example.bachelorwork.databinding.OrganisationManageItemBinding
import com.example.bachelorwork.domain.model.organisation.Organisation
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
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
                .load("${AppConstants.BASE_URL}${organisation.logoUrl}")
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: OrganisationListUiState) {
        Log.d("debug", uiState.toString())
        binding.progressBar.isVisible = uiState.isLoading && !uiState.isRefreshing
        binding.swipeRefreshLayout.isRefreshing = uiState.isRefreshing && !uiState.isLoading
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