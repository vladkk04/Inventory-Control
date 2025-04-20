package com.example.bachelorwork.ui.fragments.organisationInvitations

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentInvitationListBinding
import com.example.bachelorwork.databinding.OrganisationInvitationItemBinding
import com.example.bachelorwork.domain.model.profile.OrganisationInvitation
import com.example.bachelorwork.ui.model.organisationInvitations.InvitationListUiState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit


@AndroidEntryPoint
class InvitationListFragment : Fragment(R.layout.fragment_invitation_list) {

    private val binding by viewBinding(FragmentInvitationListBinding::bind)

    private val viewModel: InvitationListViewModel by viewModels()

    private val adapter = simpleAdapter<OrganisationInvitation, OrganisationInvitationItemBinding> {
        bind { item ->
            textViewOrganisationName.text = item.organisationName
            textViewOrganisationRole.text = getString(R.string.text_organizational_role_value, item.organisationRole.name)
            textViewInvitedBy.text = getString(R.string.text_invited_by_value, item.invitedBy)

            val futureDate = Instant.ofEpochMilli(item.expireAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            val duration = Duration.between(LocalDateTime.now(), futureDate)

            val days = duration.toDays().days.toString(DurationUnit.DAYS)
            val hours = (duration.toHours() % 24).hours.toString(DurationUnit.HOURS)

            textViewExpireIn.text = getString(R.string.text_expire_in_value,"$days & $hours")
        }
        listeners {
            buttonAccept.onClick { item ->
                viewModel.acceptInvitation(item.id)
            }
            buttonDecline.onClick { item ->
                viewModel.declineInvitation(item.id)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSwipeRefreshLayout()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: InvitationListUiState) {
        adapter.submitList(uiState.invitations)
        binding.progressBar.isGone = !uiState.isLoading && !uiState.isRefreshing
        binding.swipeRefresh.isRefreshing = uiState.isRefreshing
        binding.textViewNoInvitations.isGone = uiState.invitations.isNotEmpty() && !uiState.isLoading
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshInvitations()
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@InvitationListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

}