package com.example.inventorycotrol.ui.fragments.organisationUsers.manage

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrganisationUserManageBinding
import com.example.inventorycotrol.ui.common.adapters.ViewPagerAdapter
import com.example.inventorycotrol.ui.common.adapters.ViewPagerFragmentData
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.fragments.organisationUsers.manage.email.OrganisationUserInvitationEmailFragment
import com.example.inventorycotrol.ui.fragments.organisationUsers.manage.userId.OrganisationUserInvitationUserIdFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrganisationInvitationManagerFragment : BaseBottomSheetDialogFragment<FragmentOrganisationUserManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrganisationUserManageBinding
        get() = FragmentOrganisationUserManageBinding::inflate

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun setupViews() {
        super.setupViews()
        setupViewPagerWithTabLayout()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun setupViewPagerWithTabLayout() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        viewPagerAdapter.addFragments(
            ViewPagerFragmentData(
                OrganisationUserInvitationEmailFragment(),
                requireContext().getString(R.string.text_email),
                R.drawable.ic_email
            ),
            ViewPagerFragmentData(
                OrganisationUserInvitationUserIdFragment(),
                requireContext().getString(R.string.text_user_id),
                R.drawable.ic_identity
            )
        )

        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
            tab.setIcon(viewPagerAdapter.getIcon(position) ?: 0)
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }
}