package com.example.inventorycotrol.domain.model

enum class TimePeriod {
    TODAY, LAST_3_DAYS, LAST_7_DAYS, LAST_MONTH, LAST_90_DAYS, LAST_YEAR
}

enum class TimePeriodReports {
    TODAY, THIS_WEEK, THIS_MONTH, THIS_YEAR, YESTERDAY, PREVIOUS_WEEK, PREVIOUS_MONTH, PREVIOUS_YEAR, CUSTOM
}