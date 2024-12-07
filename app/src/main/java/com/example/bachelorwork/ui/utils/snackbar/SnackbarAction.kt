package com.example.bachelorwork.ui.utils.snackbar

data class SnackbarAction (
    val name: String,
    val action: () -> Unit,
)