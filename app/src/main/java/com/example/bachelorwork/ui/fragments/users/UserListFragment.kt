package com.example.bachelorwork.ui.fragments.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.databinding.FragmentUserListBinding
import com.example.bachelorwork.databinding.UserItemBinding
import com.example.bachelorwork.ui.model.user.UserUi
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment: Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserListViewModel by viewModels()

    private val adapter = simpleAdapter<UserUi, UserItemBinding> {
        areItemsSame = { old, new -> old.name == new.name }
        bind { user ->
            this.textViewFullName.text = "Vladyslav Klymiuk"
            this.textViewEmail.text = "vladyslav.klymiuk@tuke.sk"
        }
    }

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

        with(binding.recyclerView){
            adapter = this@UserListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        adapter.submitList(List(10) {
            UserUi(name = "Vladyslav Klymiuk")
        })
    }

}