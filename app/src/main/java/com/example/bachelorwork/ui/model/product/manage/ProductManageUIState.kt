package com.example.bachelorwork.ui.model.product.manage

import android.net.Uri

data class ProductManageUIState(
    val imageUri: Uri? = null,
    val barcode: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

