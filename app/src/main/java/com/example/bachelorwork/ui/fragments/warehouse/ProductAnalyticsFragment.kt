package com.example.bachelorwork.ui.fragments.warehouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bachelorwork.databinding.FragmentProductDetailAnalyticBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailAnalyticFragment : Fragment() {

    private var _binding: FragmentProductDetailAnalyticBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailAnalyticBinding.inflate(inflater, container, false)


        return binding.root
    }
}