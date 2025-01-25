package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.bachelorwork.databinding.FragmentProductDetailAnalyticBinding
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.views.Charts
import com.example.bachelorwork.ui.views.Charts.LegendLabelKey
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailAnalyticFragment : Fragment() {

    private var _binding: FragmentProductDetailAnalyticBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailAnalyticViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailAnalyticBinding.inflate(inflater, container, false)

        setupPriceChart()
        setupAmountChart()

        val y =
            mapOf(
                "Stock" to listOf(10, 20, 100, 300, 200, 150),
                "Restocked" to listOf(1, 20, 30, 5),
            )

        collectInLifecycle(viewModel.uiState, Lifecycle.State.STARTED) { uiState ->
            if (uiState.priceChartData.data.isNotEmpty()) {
                binding.chartViewPrice.modelProducer?.let {
                    it.runTransaction {
                        lineSeries { series(uiState.priceChartData.data.values) }
                    }
                }
            }

            binding.chartViewAmount.modelProducer?.let {
                it.runTransaction {
                    columnSeries {
                        y.values.forEach { data -> series(data) }
                        extras { extraStore -> extraStore[LegendLabelKey] = y.keys }
                    }
                }
            }
        }


        return binding.root
    }

    private fun setupAmountChart() {
        binding.chartViewAmount.apply {
            chart = Charts.createColumnChart()
            modelProducer = CartesianChartModelProducer()
        }
    }

    private fun setupPriceChart() {
        binding.chartViewPrice.apply {
            chart = Charts.createLineChart()
            modelProducer = CartesianChartModelProducer()
        }
    }


}