package com.example.inventorycotrol.ui.fragments.organisationUsers.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrganisationUserListBinding
import com.example.inventorycotrol.databinding.OrganisationUserItemBinding
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUser
import com.example.inventorycotrol.domain.model.organisation.user.OrganisationUserStatus
import com.example.inventorycotrol.ui.model.organisationUser.OrganisationUserListUiState
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.menu.createPopupMenu
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrganisationUserListFragment : Fragment(R.layout.fragment_organisation_user_list) {

    private val binding by viewBinding(FragmentOrganisationUserListBinding::bind)

    private val viewModel: OrganisationUserListViewModel by viewModels()

    private val adapter = simpleAdapter<OrganisationUser, OrganisationUserItemBinding> {
        areItemsSame = { old, new -> old.id == new.id }
        areContentsSame = { old, new -> old == new }
        bind { organisationUser ->

            if(viewModel.uiState.value.ownId == organisationUser.userId) {
                this.checkBoxManage.visibility = View.INVISIBLE
                this.textViewYou.visibility = View.VISIBLE
            }
            this.textViewFullName.text = organisationUser.organisationUserName
            this.textViewRole.text = organisationUser.organisationRole.name
            this.textViewStatus.text = organisationUser.organisationUserStatus.name
        }
        listeners {
            this.checkBoxManage.onClick { item ->
                val menu =
                    when (item.organisationUserStatus) {
                        OrganisationUserStatus.DECLINED ->
                            R.menu.popup_manage_declined_user_menu

                        OrganisationUserStatus.PENDING ->
                            R.menu.popup_manage_pending_user_menu

                        else -> R.menu.popup_manage_active_user_menu
                    }

                val isUserInactive = item.organisationUserStatus == OrganisationUserStatus.INACTIVE

                createPopupMenu(
                    requireContext(),
                    this@listeners.checkBoxManage,
                    menu
                ).apply {
                    /*this.menu.findItem(R.id.make_active)?.setVisible(isUserInactive)
                    this.menu.findItem(R.id.make_inactive)?.setVisible(!isUserInactive)*/

                    setOnMenuItemClickListener {
                        when (it.itemId) {

                            R.id.assign_role  -> {
                                viewModel.navigateToAssignRole(item)
                                true
                            }

                            R.id.edit -> {
                                viewModel.navigateToEditUser(item)
                                true
                            }

                            R.id.remove -> {
                                viewModel.deleteOrganisationUser(item.id)
                                true
                            }

                            R.id.invite_again -> {
                                viewModel.inviteUser(item)
                                true
                            }

                            R.id.cancel_invite -> {
                                item.userId?.let { userId -> viewModel.cancelInviteByUserId(userId) }
                                item.email?.let { email -> viewModel.cancelInviteByUserEmail(email) }
                                true
                            }

                            else -> true
                        }
                    }
                }.show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)
        setupFabButton()
        setupRecyclerView()
        setupToolbar()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest {
                updateUiState(it)
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupFabButton() {
        binding.fabCreateUser.setOnClickListener {
            viewModel.navigateToManageUser()
        }
    }

    private fun updateUiState(uiState: OrganisationUserListUiState) {
        binding.progressBar.visibility = if (uiState.isLoading && !uiState.isRefreshing) View.VISIBLE else View.GONE
        binding.swipeRefreshLayout.isRefreshing = uiState.isRefreshing && !uiState.isLoading

        adapter.submitList(uiState.organisationUsers)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrganisationUserListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }

}