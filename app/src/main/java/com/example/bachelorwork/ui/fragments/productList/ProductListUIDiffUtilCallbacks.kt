package com.example.bachelorwork.ui.fragments.productList

import androidx.recyclerview.widget.DiffUtil
import com.example.bachelorwork.ui.model.productList.ProductListUI

object ProductListUIDiffUtilCallbacks: DiffUtil.ItemCallback<ProductListUI>() {
    override fun areItemsTheSame(oldItem: ProductListUI, newItem: ProductListUI): Boolean = oldItem.name == newItem.name
    override fun areContentsTheSame(oldItem: ProductListUI, newItem: ProductListUI): Boolean = oldItem == newItem
    override fun getChangePayload(oldItem: ProductListUI, newItem: ProductListUI): Any? = super.getChangePayload(oldItem, newItem)
}