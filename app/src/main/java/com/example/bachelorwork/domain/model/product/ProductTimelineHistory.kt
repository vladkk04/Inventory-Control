package com.example.bachelorwork.domain.model.product


sealed class ProductTimelineHistory {

    data class ProductTimelineTimelineCreate(
        val createdAt: Int,
        val createdBy: String
    ) : ProductTimelineHistory()

    data class ProductTimelineUpdate(
        val updates: ProductUpdateHistory
    ) : ProductTimelineHistory()

}