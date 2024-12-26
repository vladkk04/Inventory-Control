package com.example.bachelorwork.ui.fragments.warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bachelorwork.databinding.FragmentProductAnalyticsItemBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductAnalyticsFragment : Fragment() {

    private var _binding: FragmentProductAnalyticsItemBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductAnalyticsItemBinding.inflate(inflater, container, false)


        return binding.root
    }
}