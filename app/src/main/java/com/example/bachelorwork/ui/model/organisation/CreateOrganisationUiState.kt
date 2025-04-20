package com.example.bachelorwork.ui.model.organisation

import android.icu.util.Currency
import com.example.bachelorwork.data.constants.AppConstants


data class CreateOrganisationUiState(
    val currencies: List<Currency> = Currency.getAvailableCurrencies()
        .filter { it.currencyCode in AppConstants.SUPPORTED_CURRENCIES },
    val isLoading: Boolean = false
) {

}
