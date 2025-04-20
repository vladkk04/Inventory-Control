package com.example.bachelorwork.ui.fragments.reports

import com.example.bachelorwork.domain.model.TimePeriodReports


data class ReportsUiState(
    val timePeriod: TimePeriodReports = TimePeriodReports.TODAY,
    val isLoading: Boolean = false,
)