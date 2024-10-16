package com.example.bachelorwork.ui.productFilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bachelorwork.databinding.FragmentProductFiltersBinding

class ProductFiltersFragment: Fragment() {
    private var _binding: FragmentProductFiltersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductFiltersBinding.inflate(inflater, container, false)

        return binding.root
    }
}