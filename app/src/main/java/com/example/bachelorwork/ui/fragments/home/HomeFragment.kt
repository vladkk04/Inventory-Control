package com.example.bachelorwork.ui.fragments.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentHomeBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        setupCustomFloatingMenu()
        setupHelloUser()

        binding.profileCirclePicture.root.setOnClickListener {
            viewModel.openNavigationDrawer()
        }

        return binding.root
    }

    private fun setupHelloUser() {
        ObjectAnimator.ofFloat(binding.textViewHelloUser, "alpha", 0f, 1f).apply {
            duration = 600
            start()
        }
        binding.textViewHelloUser.text = getString(R.string.text_hello_user, "Vladyslav")
    }

    private fun setupCustomFloatingMenu() {
        binding.customFloatingMenu.apply {
            setOnCreateItemClickListener {
                viewModel.navigateToCreateProduct()
            }
            setOnCreateOrderClickListener {
                viewModel.navigateToCreateOrder()
            }
            setOnCreateUserClickListener {
                viewModel.navigateToCreateUser()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}