package com.example.bachelorwork.ui.fragments.orderList.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.databinding.FragmentModalBottomSheetSelectProductBinding
import com.example.bachelorwork.databinding.ProductItemAddToOrderBinding
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.OrderSelectableProductUi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderProductPickerModalBottomSheetFragment: BaseBottomSheetDialogFragment<FragmentModalBottomSheetSelectProductBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetSelectProductBinding
        get() = FragmentModalBottomSheetSelectProductBinding::inflate

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    private val viewModel: OrderProductPickerViewModel by viewModels()

    override fun setupViews() {
        setupSearchBar()
        collectInLifecycle(viewModel.uiState) {
            setupUiState(it)
        }
       // hideAllComponents()

    }

    private fun setupUiState(uiState: OrderProductPickerUiState) {
        if(uiState.searchQuery.isNotEmpty()) {
            showRecyclerView()
        } else {
            hideAllComponents()
        }
        setupRecycleView()
    }

    private fun setupRecycleView() {
        val adapter = simpleAdapter<OrderSelectableProductUi, ProductItemAddToOrderBinding> {
            bind { product ->
                this.textViewName.text = product.name
            }
            listeners {
                this.root.setOnClickListener {
                    showAllOtherComponents()
                }
                this.checkboxDetail.onClick {
                    viewModel.navigateToProductDetail(index())
                }
            }
        }
        adapter.submitList(viewModel.uiState.value.products)
        with(binding.recyclerViewProducts) {
            setHasFixedSize(true)
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSearchBar() {
        binding.editTextSearch.doAfterTextChanged {
            viewModel.searchProductByName(it.toString())
        }
        binding.textInputLayoutSearch.setEndIconOnClickListener {
            viewModel.scanBarcodeScanner()
        }
    }

    private fun showRecyclerView() {
        binding.textViewSelectProduct.visibility = View.VISIBLE
        binding.recyclerViewProducts.visibility = View.VISIBLE
    }

    private fun showAllOtherComponents() {
        binding.textInputLayoutQuantity.visibility = View.VISIBLE
        binding.textInputLayoutRate.visibility = View.VISIBLE
        binding.buttonAddProduct.visibility = View.VISIBLE
    }

    private fun hideAllComponents() {
        binding.textViewSelectProduct.visibility = View.GONE
        binding.recyclerViewProducts.visibility = View.GONE
        binding.textInputLayoutQuantity.visibility = View.GONE
        binding.textInputLayoutRate.visibility = View.GONE
        binding.buttonAddProduct.visibility = View.GONE
    }
}