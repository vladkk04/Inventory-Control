package com.example.inventorycotrol.ui.snackbar

data class SnackbarEvent(
    val message: String,
    val action: SnackbarAction? = null,
    val appearanceConfig: SnackbarAppearanceConfig = SnackbarAppearanceConfig(),
)
