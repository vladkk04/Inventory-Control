package com.example.bachelorwork.ui.fragments.orders.manage.manageProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.SimpleBindingAdapter
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrderProductManageBinding
import com.example.bachelorwork.databinding.OrderCreateSearchableProductBinding
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.fragments.orders.manage.OrderManageProductSharedViewModel
import com.example.bachelorwork.ui.model.order.OrderSearchableProductListUi
import com.example.bachelorwork.ui.model.order.product.OrderManageProductFormUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.utils.extensions.hiltViewModelNavigation
import com.example.bachelorwork.ui.utils.extensions.toDp

abstract class BaseOrderProductManageFragment: BaseBottomSheetDialogFragment<FragmentOrderProductManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderProductManageBinding
        get() = FragmentOrderProductManageBinding::inflate

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    private val sharedViewModel: OrderManageProductSharedViewModel by hiltViewModelNavigation(
        Destination.CreateOrder)

    private val adapter: SimpleBindingAdapter<OrderSearchableProductListUi> =
        simpleAdapter<OrderSearchableProductListUi, OrderCreateSearchableProductBinding> {
            areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
            areContentsSame =
                { oldItem, newItem -> oldItem == newItem && index(oldItem) == index(newItem) }

            bind { item ->
                this.textViewName.text = item.name

                if (item.isSelected) {
                    this.layout.alpha = 0.5f
                    this.cardView.strokeWidth = 2.toDp(requireContext())
                } else {
                    this.layout.alpha = 1f
                    this.cardView.strokeWidth = 0
                }
            }
            listeners {
                root.onClick { product ->
                    binding.textInputQuantity.hint = getString(R.string.hint_quantity_unit, product.unit)
                    //viewModel.selectItem(product.id)
                }
                checkBoxDetail.onClick { product ->
                    //viewModel.navigateToProductDetail(product.id)
                }
            }
        }

    private fun setupRecycleView() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = this@BaseOrderProductManageFragment.adapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupFormUiState(uiState: OrderManageProductFormUiState) {
        binding.textInputQuantity.error = uiState.quantityError
        binding.textInputRate.error = uiState.rateError
    }


    private fun setupSearchBar() {
        binding.editTextSearch.doAfterTextChanged {
            //viewModel.onSearchQueryChanged(it.toString())
        }
        binding.textInputSearch.setEndIconOnClickListener {
           // viewModel.scanBarcodeScanner()
        }
    }

    private fun setupEditTextForm() {
        binding.editTextQuantity.doAfterTextChanged {
           //viewModel.onEvent(OrderManageProductFormEvent.Quantity(it.toString()))
        }
        binding.editTextRate.doAfterTextChanged {
            //viewModel.onEvent(OrderManageProductFormEvent.Rate(it.toString()))
        }
    }

    private fun setupAddButton() {
        binding.buttonManageProduct.text = getString(R.string.text_add_product)
        binding.buttonManageProduct.setOnClickListener {
           /* viewModel.getSelectedProductData()?.let {
                sharedViewModel.addProduct(it.copy())
            }*/
        }
    }
}