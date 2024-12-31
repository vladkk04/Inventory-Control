package com.example.bachelorwork.ui.fragments.orderList.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.bachelorwork.databinding.FragmentModalBottomSheetOrderManageBinding
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderCreateModalBottomSheetFragment: BaseBottomSheetDialogFragment<FragmentModalBottomSheetOrderManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetOrderManageBinding
        get() = FragmentModalBottomSheetOrderManageBinding::inflate

    override val titleToolbar: String = "Create Order"

    private val viewModel: OrderCreateViewModel by viewModels()

    override fun setupViews() {
        setupSelectedProductList()
    }

    private fun setupSelectedProductList() {
        binding.productListSelected.setOnClickListener {
            viewModel.navigateToOrderProductPicker()
        }
    }
}