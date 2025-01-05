package com.example.bachelorwork.ui.fragments.orderList.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.SimpleBindingAdapter
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetOrderAddProductBinding
import com.example.bachelorwork.databinding.ProductItemInOrderSelectionBinding
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.OrderSelectableProductUi
import com.example.bachelorwork.ui.utils.extensions.toDp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderAddProductModalBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentModalBottomSheetOrderAddProductBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetOrderAddProductBinding
        get() = FragmentModalBottomSheetOrderAddProductBinding::inflate

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    private val viewModel: OrderAddProductViewModel by viewModels()

    private val adapter: SimpleBindingAdapter<OrderSelectableProductUi> =
        simpleAdapter<OrderSelectableProductUi, ProductItemInOrderSelectionBinding> {
            areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
            areContentsSame = { oldItem, newItem -> oldItem == newItem && index(oldItem) == index(newItem) }

            bind { item ->
                this.textViewName.text = item.name

                Log.d("debug", "a ${item}")

                if (item.isSelected) {
                    this.layout.alpha = 0.5f
                    this.cardView.strokeWidth = 2.toDp(requireContext())
                    showAllOtherComponents()
                    Log.d("debug", "d")
                } else {
                    this.layout.alpha = 1f
                    this.cardView.strokeWidth = 0
                    hideAllComponents()
                    Log.d("debug", "c")
                }
            }
            listeners {
                root.onClick {
                    Log.d("debug", "f ${index()}")
                    viewModel.selectItem(index())
                }
                checkboxDetail.onClick { product ->
                    viewModel.navigateToProductDetail(product.id)
                }
            }
        }


    override fun setupViews() {
        setupSearchBar()
        setupRecycleView()
        setupInputForm()
        setupAddButton()

        collectInLifecycle(viewModel.uiState) {
            setupUiState(it)
        }
        collectInLifecycle(viewModel.uiFormState) {
            setupFormUiState(it)
        }
    }


    private fun setupUiState(uiState: OrderAddProductUiState) {
        if (uiState.products.isEmpty() && uiState.searchQuery.isNotEmpty()) {
            binding.textViewEnterOrScan.text = getString(R.string.text_no_result)
        } else {
            binding.textViewEnterOrScan.text = getString(R.string.text_enter_or_scan)
        }

        if (uiState.searchQuery.isNotEmpty() && uiState.products.isNotEmpty()) {
            adapter.submitList(uiState.products) {
                binding.recyclerViewProducts.visibility = View.VISIBLE
                binding.textViewEnterOrScan.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupFormUiState(uiState: OrderAddProductFormUiState) {
        binding.textInputLayoutQuantity.error = uiState.quantityError
        binding.textInputLayoutRate.error = uiState.rateError
    }

    private fun setupRecycleView() {
        with(binding.recyclerViewProducts) {
            setHasFixedSize(true)
            adapter = this@OrderAddProductModalBottomSheetFragment.adapter
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

    private fun setupInputForm() {
        binding.textInputLayoutQuantity.editText?.doAfterTextChanged {
            viewModel.onEvent(OrderAddProductFormEvent.Quantity(it.toString()))
        }
        binding.textInputLayoutRate.editText?.doAfterTextChanged {
            viewModel.onEvent(OrderAddProductFormEvent.Rate(it.toString()))
        }
    }

    private fun setupAddButton() {
        binding.buttonAddProduct.setOnClickListener {
            viewModel.addProduct()
        }
    }

    private fun showAllOtherComponents() {
        binding.textInputLayoutQuantity.visibility = View.VISIBLE
        binding.textInputLayoutRate.visibility = View.VISIBLE
        binding.buttonAddProduct.visibility = View.VISIBLE
    }

    private fun hideAllComponents() {
        binding.textInputLayoutQuantity.visibility = View.GONE
        binding.textInputLayoutRate.visibility = View.GONE
        binding.buttonAddProduct.visibility = View.GONE
    }
}