package com.example.bachelorwork.ui.snackbar

data class SnackbarAction (
    val name: String,
    val action: () -> Unit,
)