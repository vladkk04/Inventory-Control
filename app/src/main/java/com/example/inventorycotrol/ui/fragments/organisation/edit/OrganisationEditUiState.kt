package com.example.inventorycotrol.ui.fragments.organisation.edit

import android.icu.util.Currency
import com.example.inventorycotrol.data.constants.AppConstants


data class OrganisationEditUiState(
    val currencies: List<Currency> = Currency.getAvailableCurrencies()
        .filter { it.currencyCode in AppConstants.SUPPORTED_CURRENCIES },
    val isLoading: Boolean = false,
)