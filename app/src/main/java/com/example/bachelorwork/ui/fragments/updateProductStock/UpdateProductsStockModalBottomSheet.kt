package com.example.bachelorwork.ui.fragments.updateProductStock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetUpdateProductsStockBinding
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProductsStockModalBottomSheet: BaseBottomSheetDialogFragment<FragmentModalBottomSheetUpdateProductsStockBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetUpdateProductsStockBinding
        get() = FragmentModalBottomSheetUpdateProductsStockBinding::inflate

    override val titleToolbar: String = "Update Stock"

    override val inflateMenu: Int
        get() = R.menu.bottom_sheet_update_product_stock_menu

    private val viewModel: UpdateProductsStockViewModel by viewModels()

    override fun setupViews() {

    }
}
