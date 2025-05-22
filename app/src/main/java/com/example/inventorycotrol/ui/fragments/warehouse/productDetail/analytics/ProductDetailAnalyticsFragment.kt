package com.example.inventorycotrol.ui.fragments.warehouse.productDetail.analytics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentProductDetailAnalyticsBinding
import com.example.inventorycotrol.domain.model.TimePeriod
import com.example.inventorycotrol.ui.views.Charts
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.Fill
import com.patrykandpatrick.vico.core.common.HorizontalLegend
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.component.ShapeComponent
import com.patrykandpatrick.vico.core.common.component.TextComponent
import com.patrykandpatrick.vico.core.common.data.ExtraStore
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import com.patrykandpatrick.vico.views.cartesian.ScrollHandler
import com.patrykandpatrick.vico.views.cartesian.ZoomHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class ProductDetailAnalyticsFragment : Fragment() {

    private var _binding: FragmentProductDetailAnalyticsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailAnalyticsViewModel by viewModels({ requireParentFragment() })

    private val chartStockChangeModelProducer = CartesianChartModelProducer()
    private val chartVolumeStockModelProducer = CartesianChartModelProducer()

    private val bottomStockChangeExtraStoreKey = ExtraStore.Key<List<String>>()
    private val bottomStockVolumeStoreKey = ExtraStore.Key<List<String>>()
    private val stockVolumeLegendKey = ExtraStore.Key<Set<String>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailAnalyticsBinding.inflate(inflater, container, false)

        setupStockChangeChart()
        setupStockVolumeChart()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                binding.progressBarStockChange.isVisible = uiState.isLoading
                binding.progressBarStockVolume.isVisible = uiState.isLoading

                binding.textViewNoChangesStockChange.isVisible =
                    uiState.stockChangeChartData.data.isEmpty()
                binding.textViewNoChangesStockVolume.isVisible =
                    uiState.volumeStockChartData.x.isEmpty() || uiState.volumeStockChartData.y.isEmpty()

                if (uiState.stockChangeChartData.data.isNotEmpty()) {
                    val data = uiState.stockChangeChartData.data
                    binding.chartViewPrice.visibility = View.VISIBLE
                    chartStockChangeModelProducer.createTransaction().apply {
                        lineSeries { series(data.values) }
                        extras { it[bottomStockChangeExtraStoreKey] = data.keys.toList() }
                    }.commit()
                } else {
                    binding.chartViewPrice.visibility = View.INVISIBLE
                }

                if (uiState.volumeStockChartData.x.isNotEmpty() && uiState.volumeStockChartData.y.isNotEmpty()) {
                    binding.chartViewVolume.visibility = View.VISIBLE
                    val x = uiState.volumeStockChartData.x
                    val y = uiState.volumeStockChartData.y

                    chartVolumeStockModelProducer.createTransaction().apply {
                        columnSeries { y.values.forEach { series(x, it) } }
                        extras { it[stockVolumeLegendKey] = y.keys.toSet() }
                        extras { it[bottomStockVolumeStoreKey] = x.map { hell -> hell.toString() } }
                    }.commit()
                } else {
                    binding.chartViewVolume.visibility = View.INVISIBLE
                }
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getStateFlow(
            "datePeriod",
            TimePeriod.TODAY
        )?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                it.collectLatest { datePeriod ->
                    val text = when (datePeriod) {
                        TimePeriod.TODAY -> getString(R.string.text_selected_date_today_value)
                        TimePeriod.LAST_3_DAYS -> getString(
                            R.string.text_selected_date_period_in_days_value,
                            "3"
                        )

                        TimePeriod.LAST_7_DAYS -> getString(
                            R.string.text_selected_date_period_in_days_value,
                            "7"
                        )

                        TimePeriod.LAST_MONTH -> getString(
                            R.string.text_selected_date_period_in_days_value,
                            "30"
                        )

                        TimePeriod.LAST_90_DAYS -> getString(
                            R.string.text_selected_date_period_in_days_value,
                            "90"
                        )

                        TimePeriod.LAST_YEAR -> getString(R.string.text_selected_date_last_year)
                    }

                    binding.textViewDateStockChanges.text = text
                    binding.textViewDateStockVolume.text = text
                    viewModel.changeStockDatePeriod(datePeriod)
                }
            }
        }


        return binding.root
    }

    private fun setupStockVolumeChart() {
        binding.chartViewVolume.apply {
            val color = listOf(Color.GREEN, Color.RED)

            chart = Charts().createColumnChart(
                legend = HorizontalLegend(
                    items = { extraStore ->
                        extraStore[stockVolumeLegendKey].forEachIndexed { index, label ->
                            add(
                                LegendItem(
                                    icon = ShapeComponent(
                                        fill = Fill(color[index]),
                                        shape = CorneredShape.Pill
                                    ),
                                    TextComponent(
                                        color = Color.WHITE,
                                    ),
                                    label,
                                )
                            )
                        }
                    },
                    padding = Dimensions(8f, 4f),
                ),
                bottomCartesianValueFormatter = { context, _, _ ->
                    val data = context.model.extraStore[bottomStockVolumeStoreKey]
                    val result = data.let {
                        if (it.size >= 2) {
                            val startDate =
                                Date(Instant.ofEpochMilli(it[0].toLong()).toEpochMilli())
                            val endDate = Date(Instant.ofEpochMilli(it[1].toLong()).toEpochMilli())

                            val startFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                            val endFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

                            "${startFormat.format(startDate)} - ${endFormat.format(endDate)}"
                        } else {
                            "N/A"
                        }
                    }

                    result
                }
            )
            zoomHandler = ZoomHandler(true)
            scrollHandler = ScrollHandler(true)
            modelProducer = chartVolumeStockModelProducer
        }

        binding.textViewDateStockVolume.setOnClickListener {
            viewModel.openSelectorPeriodDate()
        }
    }


    private fun setupStockChangeChart() {
        binding.chartViewPrice.apply {
            chart = Charts().createLineChart(
                bottomCartesianValueFormatter = { context, value, _ ->
                    val value2 =
                        context.model.extraStore[bottomStockChangeExtraStoreKey][value.toInt()]
                    val gg = when (viewModel.uiState.value.datePeriodChangeStock) {
                        TimePeriod.TODAY -> {
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                        }

                        else -> {
                            SimpleDateFormat("MM.dd 'at' HH:mm:ss", Locale.getDefault())
                        }
                    }
                    gg.format(Date(value2.toLong()))
                }
            )
            zoomHandler = ZoomHandler(true)
            scrollHandler = ScrollHandler(true)
            modelProducer = chartStockChangeModelProducer
        }

        binding.textViewDateStockChanges.setOnClickListener {
            viewModel.openSelectorPeriodDate()
        }
    }

}