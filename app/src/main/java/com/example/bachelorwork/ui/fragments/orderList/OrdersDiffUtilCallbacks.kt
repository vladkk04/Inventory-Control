package com.example.bachelorwork.ui.fragments.orderList

import androidx.recyclerview.widget.DiffUtil
import com.example.bachelorwork.domain.model.order.Order

object OrdersDiffUtilCallbacks: DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: Order, newItem: Order): Any? = super.getChangePayload(oldItem, newItem)
}