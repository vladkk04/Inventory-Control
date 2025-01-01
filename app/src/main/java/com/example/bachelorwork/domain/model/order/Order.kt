package com.example.bachelorwork.domain.model.order

import com.example.bachelorwork.ui.constant.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Order(
    val id: Int = ((100000..999999).random().toString() + System.currentTimeMillis()
        .toString()).take(7).toInt(),
    val date: String = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("${Constants.DEFAULT_DATE_FORMAT_PATTERN} 'at' ${Constants.DEFAULT_TIME_FORMAT_WITH_AM_PM}")),
    val items: List<OrderSubItem>
) {
    val total = items.sumOf { it.price * it.quantity }
}
