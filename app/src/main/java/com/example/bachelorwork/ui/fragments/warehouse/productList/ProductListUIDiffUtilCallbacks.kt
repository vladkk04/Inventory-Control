package com.example.bachelorwork.ui.fragments.warehouse.productList

import androidx.recyclerview.widget.DiffUtil
import com.example.bachelorwork.ui.model.productList.ProductListItemUI

object ProductListUIDiffUtilCallbacks: DiffUtil.ItemCallback<ProductListItemUI>() {
    override fun areItemsTheSame(oldItem: ProductListItemUI, newItem: ProductListItemUI): Boolean = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: ProductListItemUI, newItem: ProductListItemUI): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: ProductListItemUI, newItem: ProductListItemUI): Any? = super.getChangePayload(oldItem, newItem)
}