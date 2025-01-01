package com.example.bachelorwork.ui.fragments.more.userList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachelorwork.databinding.FragmentUserListBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment: Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserListViewModel by viewModels()

    private lateinit var adapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

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
        adapter = UserListAdapter()

        with(binding.recyclerViewManageUsers){
            adapter = this@UserListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}