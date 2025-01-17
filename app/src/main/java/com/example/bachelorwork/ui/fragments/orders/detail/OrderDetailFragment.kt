package com.example.bachelorwork.ui.fragments.orders.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bachelorwork.databinding.FragmentOrderDetailBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment: Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        return binding.root
    }
}