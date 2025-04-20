package com.example.bachelorwork.ui.fragments.productUpdateStock

import com.example.bachelorwork.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductUpdateStockOutFragment(override val operationType: StockOperationType = StockOperationType.STOCK_OUT) : BaseUpdateStockFragment() {

    override val inputQuantityHint: String
        get() = getString(R.string.text_outgoing_stock_hint)

    override val titleToolbar: String
        get() = "Stock Out"
}