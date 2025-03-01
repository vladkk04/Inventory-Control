package com.example.bachelorwork.ui.fragments.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentHomeBinding
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHelloUser()

    }

    private fun setupHelloUser() {
        ObjectAnimator.ofFloat(binding.textViewHelloUser, "alpha", 0f, 1f).apply {
            duration = 600
            start()
        }
        binding.textViewHelloUser.text = getString(R.string.text_hello_user, "Vladyslav")
    }

}