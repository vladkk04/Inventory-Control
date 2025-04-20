package com.example.bachelorwork.ui.fragments.productUpdateStock

import com.example.bachelorwork.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductUpdateStockInFragment(override val operationType: StockOperationType = StockOperationType.STOCK_IN) : BaseUpdateStockFragment() {

    override val inputQuantityHint: String
        get() = getString(R.string.text_incoming_stock_hint)

    override val titleToolbar: String
        get() = "Stock In"


}