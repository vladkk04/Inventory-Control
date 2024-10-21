package com.example.bachelorwork.ui.productList

import androidx.recyclerview.widget.DiffUtil
import com.example.bachelorwork.ui.model.ProductUI

object ProductDiffUtilCallbacks: DiffUtil.ItemCallback<ProductUI>() {
    override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: ProductUI, newItem: ProductUI): Any? = super.getChangePayload(oldItem, newItem)
}