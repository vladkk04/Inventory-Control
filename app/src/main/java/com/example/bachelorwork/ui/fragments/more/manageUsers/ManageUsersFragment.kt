package com.example.bachelorwork.ui.fragments.more.manageUsers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachelorwork.databinding.FragmentManageUsersBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageUsersFragment: Fragment() {

    private var _binding: FragmentManageUsersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ManageUsersViewModel by viewModels()

    private lateinit var adapter: ManageUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageUsersBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)
        setupFabButton()
        setupRecyclerView()

        return binding.root
    }

    private fun setupFabButton() {
        binding.fabCreateUser.setOnClickListener {
            viewModel.navigateToCreateUser()
        }
    }

    private fun setupRecyclerView() {
        adapter = ManageUsersAdapter()

        with(binding.recyclerViewManageUsers){
            adapter = this@ManageUsersFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}