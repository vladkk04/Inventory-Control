package com.example.inventorycotrol.ui.snackbar

data class SnackbarAction (
    val name: String,
    val action: () -> Unit,
)