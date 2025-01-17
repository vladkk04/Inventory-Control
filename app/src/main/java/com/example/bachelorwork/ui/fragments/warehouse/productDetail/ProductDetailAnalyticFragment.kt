package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bachelorwork.databinding.FragmentProductDetailAnalyticBinding
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProductDetailAnalyticFragment : Fragment() {

    private var _binding: FragmentProductDetailAnalyticBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailAnalyticViewModel by viewModels({ requireParentFragment() })

    val modelProducer = CartesianChartModelProducer()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailAnalyticBinding.inflate(inflater, container, false)


        binding.chartViewPrice.modelProducer = modelProducer

        viewLifecycleOwner.lifecycleScope.launch {
            modelProducer.runTransaction { lineSeries { series(3.4, 4, 5, 5) } }
        }


        return binding.root
    }
}