package com.example.inventorycotrol.ui.fragments.reports

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentReportsBinding
import com.example.inventorycotrol.domain.model.TimePeriodReports
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.utils.PdfReportGenerator
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.viewBinding
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.example.inventorycotrol.util.namesTyped
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate


@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private val binding by viewBinding(FragmentReportsBinding::bind)

    private val viewModel: ReportsViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            mainViewModel.isConnected.collectLatest {
                binding.buttonDownload.isEnabled = it
            }
        }

        setupToolbar()
        setupButtonDownload()

        setupAutoCompleteTextView()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: ReportsUiState) {
        binding.autoCompleteDate.setText(uiState.timePeriod.name.replace('_', ' '), false)
        binding.textInputDate.helperText = viewModel.getDateRangeString()
    }

    private fun setupAutoCompleteTextView() {
        (binding.autoCompleteDate as? MaterialAutoCompleteTextView)?.setSimpleItems(
            TimePeriodReports.entries.namesTyped().map { it.replace('_', ' ') }.toTypedArray()
        )

        binding.autoCompleteDate.setOnItemClickListener { _, _, i, _ ->
            val timePeriod = TimePeriodReports.entries[i]

            if (timePeriod == TimePeriodReports.CUSTOM) {
                AppDialogs.createShowDateRangePicker {
                    viewModel.setCustomDateRange(it)
                    binding.textInputDate.helperText = viewModel.getDateRangeString()
                }.show(parentFragmentManager, "date_range_picker")
            } else {
                viewModel.onTimePeriodSelected(timePeriod)
            }

            binding.textInputDate.helperText = viewModel.getDateRangeString()
        }
    }


    private fun setupButtonDownload() {
        binding.buttonDownload.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val pdfReportGenerator = PdfReportGenerator()
                    val reportData = viewModel.getReportData()

                    pdfReportGenerator.generateStockReport(
                        creationDate = LocalDate.now().toString(),
                        periodReports = viewModel.uiState.value.timePeriod,
                        products = reportData,
                        dataRange = viewModel.getDateRangeString(),
                        callback = { file ->
                            if (file != null) {
                                Snackbar.make(
                                    binding.root,
                                    "Report downloaded successfully to folder Download",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    "Failed to generate report",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    )
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.root,
                        "Error generating report: ${e.message}",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}