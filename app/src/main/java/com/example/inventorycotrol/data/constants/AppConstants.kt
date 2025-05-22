package com.example.inventorycotrol.data.constants

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object AppConstants {
    const val PRODUCT_TIMELINE_HISTORY_FORMAT = "MMM dd, yyyy HH:mm"
    const val ORDER_DATE_FORMAT = "MMM dd, yyyy h:mm a"

    val SUPPORTED_CURRENCIES = setOf(
        "EUR", "GBP", "CHF", "SEK", "NOK", "DKK", "PLN", "HUF", "CZK", "RON", "BGN", "HRK",
        "ISK", "RSD", "UAH", "USD"
    )

    val SUPPORTED_MIME_TYPES_FOR_PICKER = arrayOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "text/plain",
        "image/*"
    )

    val CURRENCY_SYMBOLS = mapOf(
        "EUR" to "€",
        "GBP" to "£",
        "CHF" to "CHF",
        "SEK" to "kr",
        "NOK" to "kr",
        "DKK" to "kr",
        "PLN" to "zł",
        "HUF" to "Ft",
        "CZK" to "Kč",
        "RON" to "lei",
        "BGN" to "лв",
        "HRK" to "kn",
        "ISK" to "kr",
        "RSD" to "дин",
        "UAH" to "₴",
        "USD" to "$"
    )


    val USER_ID_KEY = stringPreferencesKey("user_id")
    val USER_ROLE = stringPreferencesKey("user_role")

    val JWT_ACCESS_TOKEN_KEY = stringPreferencesKey("jwt_access_token")
    val JWT_REFRESH_TOKEN_KEY = stringPreferencesKey("jwt_refresh_token")
    val RESET_PASSWORD_TOKEN_KEY = stringPreferencesKey("reset_password_token")
    val SELECTED_ORGANISATION_ID = stringPreferencesKey("selected_organisation_id")

    val NOTIFICATION_DAYS = stringSetPreferencesKey("notification_days")
    val NOTIFY_ONLY = stringSetPreferencesKey("notification_only")
    val NOTIFICATION_TIME = stringPreferencesKey("notification_time")

    val ORGANISATION_CURRENCY = stringPreferencesKey("organisation_currency")

    val NORMAL_THRESHOLD_PERCENTAGE = doublePreferencesKey("normal_threshold_percentage")
    val MEDIUM_THRESHOLD_PERCENTAGE = doublePreferencesKey("medium_threshold_percentage")
    val CRITICAL_THRESHOLD_PERCENTAGE = doublePreferencesKey("critical_threshold_percentage")

    const val BASE_URL = "http://inventory-control.click" //
    const val BASE_URL_CLOUD_FRONT = "https://d27ms6sfcj15c2.cloudfront.net"
}