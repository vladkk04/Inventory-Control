package com.example.bachelorwork.domain.model.product

import androidx.annotation.DrawableRes
import com.example.bachelorwork.R
import kotlinx.serialization.Serializable

@Serializable
data class ProductTag(
    val name: String,
    @DrawableRes
    val icon: Int = R.drawable.ic_tag,
)
