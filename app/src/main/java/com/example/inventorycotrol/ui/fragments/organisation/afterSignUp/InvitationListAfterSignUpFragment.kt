package com.example.inventorycotrol.ui.fragments.organisation.afterSignUp

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentInvitationListAfterSignUpBinding
import com.example.inventorycotrol.databinding.OrganisationInvitationItemBinding
import com.example.inventorycotrol.domain.model.profile.OrganisationInvitation
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit


@AndroidEntryPoint
class InvitationListAfterSignUpFragment :
    Fragment(R.layout.fragment_invitation_list_after_sign_up) {

    private val binding by viewBinding(FragmentInvitationListAfterSignUpBinding::bind)

    private val viewModel: InvitationListAfterSignUpViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private val adapter = simpleAdapter<OrganisationInvitation, OrganisationInvitationItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { organisationInvitation ->
            textViewOrganisationName.text = getString(
                R.string.text_organisation_short_name_value,
                organisationInvitation.organisationName
            )
            textViewOrganisationRole.text = getString(
                R.string.text_role_value,
                organisationInvitation.organisationRole.name.replaceFirstChar(Char::titlecaseChar)
            )
            textViewInvitedBy.text = getString(R.string.text_invited_by_value, organisationInvitation.invitedBy)

            val futureDate = Instant.ofEpochMilli(organisationInvitation.expireAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

            val duration = Duration.between(LocalDateTime.now(), futureDate)

            val days = duration.toDays().days.toString(DurationUnit.DAYS)
            val hours = (duration.toHours() % 24).hours.toString(DurationUnit.HOURS)

            textViewExpireIn.text = getString(R.string.text_expire_in_value, "$days & $hours")
        }
        listeners {
            buttonAccept.onClick { organisationInvitation ->
                viewModel.acceptInvitation(organisationInvitation.id)
            }
            buttonDecline.onClick { organisationInvitation ->
                viewModel.declineInvitation(organisationInvitation.id)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupRecyclerView()
        setupRefreshLayout()
        setupButtons()
        setupToolbar()

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.buttonSwitchToOrganisation.isEnabled = it
                binding.buttonCreateOrganisation.isEnabled = it
            }
        }

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: InvitationListAfterSignUpUiState) {
        binding.swipeRefresh.isRefreshing = uiState.isRefreshing
        binding.progressBar.isVisible = uiState.isLoading && !uiState.isRefreshing
        adapter.submitList(uiState.invitations)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@InvitationListAfterSignUpFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sign_out -> {
                    AppDialogs.createSignOutDialog(requireContext()) {
                        viewModel.signOut()
                    }.show()
                    true
                }

                else -> false
            }
        }
    }

    private fun setupRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshInvitations()
        }
    }

    private fun setupButtons() {
        binding.buttonSwitchToOrganisation.setOnClickListener {
            viewModel.navigateToOrganisationList()
        }
        binding.buttonCreateOrganisation.setOnClickListener {
            viewModel.navigateToCreateOrganisation()
        }
    }
}