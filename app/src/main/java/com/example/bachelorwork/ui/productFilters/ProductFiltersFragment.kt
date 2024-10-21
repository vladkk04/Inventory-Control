package com.example.bachelorwork.ui.productFilters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
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
        setupToolbarInsets()
        return binding.root
    }

    private fun setupToolbarInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarFilters) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
    }
}