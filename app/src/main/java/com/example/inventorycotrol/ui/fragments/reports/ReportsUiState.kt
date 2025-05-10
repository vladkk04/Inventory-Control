package com.example.inventorycotrol.ui.fragments.reports

import com.example.inventorycotrol.domain.model.TimePeriodReports


data class ReportsUiState(
    val timePeriod: TimePeriodReports = TimePeriodReports.TODAY,
    val isLoading: Boolean = false,
)