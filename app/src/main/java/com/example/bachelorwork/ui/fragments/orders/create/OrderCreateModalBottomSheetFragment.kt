package com.example.bachelorwork.ui.fragments.orders.create

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.databinding.FragmentModalBottomSheetOrderManageBinding
import com.example.bachelorwork.databinding.ProductItemInOrderCreationBinding
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.order.OrderAddedProductUi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderCreateModalBottomSheetFragment: BaseBottomSheetDialogFragment<FragmentModalBottomSheetOrderManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetOrderManageBinding
        get() = FragmentModalBottomSheetOrderManageBinding::inflate

    override val titleToolbar: String = "Create Order"

    private val viewModel: OrderCreateViewModel by viewModels()

    private val sharedViewModel: OrderAddProductSharedViewModel by viewModels()

    private val adapter = simpleAdapter<OrderAddedProductUi, ProductItemInOrderCreationBinding> {
        bind {
            this.textViewName.text = it.name
            this.textViewAmount.text = "2"
            this.textViewTotal.text = it.total.toString()
        }
    }

    override fun setupViews() {
        setupRecyclerView()
        collectInLifecycle(sharedViewModel.selectedProduct) {
           // it?.let { viewModel.addProductToOrder(it) }
        }

        collectInLifecycle(viewModel.uiState) {
            setupUiState(it)
        }
    }

    private fun setupUiState(uiState: OrderCreateUiState) {
        adapter.submitList(uiState.addedProduct)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrderCreateModalBottomSheetFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}