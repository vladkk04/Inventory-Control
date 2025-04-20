package com.example.bachelorwork.data.constants

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object AppConstants {
    const val PRODUCT_TIMELINE_HISTORY_FORMAT = "MMM dd, yyyy h:mm a"
    const val ORDER_DATE_FORMAT = "MMM dd, yyyy h:mm a"

    val SUPPORTED_CURRENCIES = setOf(
        "EUR", "GBP", "CHF", "SEK", "NOK", "DKK", "PLN", "HUF", "CZK", "RON", "BGN", "HRK",
        "ISK", "RSD", "UAH", "USD"
    )


    val USER_ID_KEY = stringPreferencesKey("user_id")
   /* val USER_FULL_NAME = stringPreferencesKey("user_full_name")
    val USER_ORGANISATION_NAME = stringPreferencesKey("user_organisation_name")
    val USER*/
    val USER_PREFERRED_ORGANISATION_ID = stringPreferencesKey("user_preferred_organisation_id")
    val JWT_ACCESS_TOKEN_KEY = stringPreferencesKey("jwt_access_token")
    val JWT_REFRESH_TOKEN_KEY = stringPreferencesKey("jwt_refresh_token")
    val RESET_PASSWORD_TOKEN_KEY = stringPreferencesKey("reset_password_token")
    val SELECTED_ORGANISATION_ID = stringPreferencesKey("selected_organisation_id")
    val NOTIFICATION_TIME = stringSetPreferencesKey("notification_time")

    const val BASE_URL = "http://192.168.68.60:8080"
}