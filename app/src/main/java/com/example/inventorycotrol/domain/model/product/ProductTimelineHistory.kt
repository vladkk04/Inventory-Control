package com.example.inventorycotrol.domain.model.product


sealed class ProductTimelineHistory {

    data class ProductTimelineTimelineCreate(
        val createdAt: Long,
        val createdBy: String
    ) : ProductTimelineHistory()

    data class ProductTimelineUpdate(
        val updates: ProductUpdateHistory
    ) : ProductTimelineHistory()

}