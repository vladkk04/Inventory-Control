package com.example.bachelorwork.ui.fragments.reports

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentReportsBinding
import com.example.bachelorwork.domain.model.TimePeriodReports
import com.example.bachelorwork.ui.utils.PdfReportGenerator
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.example.bachelorwork.util.namesTyped
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReportsFragment : Fragment(R.layout.fragment_reports) {

    private val binding by viewBinding(FragmentReportsBinding::bind)

    private val viewModel: ReportsViewModel by viewModels()

    private lateinit var pdfReportGenerator: PdfReportGenerator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithPadding(binding.root)


        setupToolbar()
        setupButtonDownload()

        setupAutoCompleteTextView()

        collectInLifecycle(viewModel.uiState) { state ->
            updateUiState(state)
        }
    }

    private fun updateUiState(uiState: ReportsUiState) {
        binding.autoCompleteDate.setText(uiState.timePeriod.name.replace('_', ' '), false)
    }

    private fun setupAutoCompleteTextView() {
        (binding.autoCompleteDate as? MaterialAutoCompleteTextView)?.setSimpleItems(
            TimePeriodReports.entries.namesTyped().map { it.replace('_', ' ') }.toTypedArray()
        )

        binding.autoCompleteDate.setOnItemClickListener { adapterView, view, i, l ->
            viewModel.onTimePeriodSelected(TimePeriodReports.entries[i])
        }
    }

    private fun setupDatePicker() {

    }

    private fun setupButtonDownload() {
        binding.buttonDownload.setOnClickListener {
            pdfReportGenerator = PdfReportGenerator(requireContext())

            val products = viewModel.getReport()


            Log.d("debug", products.toString())

            pdfReportGenerator.generateStockReport(
                organizationName = "Organization Name",
                creationDate = "Creation Date",
                periodReports = viewModel.uiState.value.timePeriod,
                products = products,
                callback = { file ->
                    Snackbar.make(
                        binding.root,
                        "Report downloaded successfully to folder Download",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            )

        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateBack()
        }
    }
}