package com.example.bachelorwork.domain.model.product

import com.example.bachelorwork.data.local.serialization.DateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date


@Serializable
sealed class ProductTimelineHistory {

    @Serializable
    @SerialName("create")
    data class ProductTimelineCreate(
        @Serializable(with = DateSerializer::class)
        val createdAt: Date,
        val createdBy: String,
    ) : ProductTimelineHistory()

    @Serializable
    @SerialName("update")
    data class ProductTimelineUpdate(
        @Serializable(with = DateSerializer::class)
        val updatedAt: Date,
        val updatedBy: String
    ): ProductTimelineHistory()
}





