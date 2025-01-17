package com.example.bachelorwork.ui.fragments.orders.create.manage.editAddedProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.SimpleBindingAdapter
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetOrderManageProductBinding
import com.example.bachelorwork.databinding.ProductItemInOrderSelectionBinding
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.fragments.orders.create.OrderManageProductSharedViewModel
import com.example.bachelorwork.ui.model.order.OrderSelectableProductListUi
import com.example.bachelorwork.ui.model.order.create.manage.product.OrderManageProductFormEvent
import com.example.bachelorwork.ui.model.order.create.manage.product.OrderManageProductFormUiState
import com.example.bachelorwork.ui.model.order.create.manage.product.OrderManageProductUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.hiltViewModelNavigation
import com.example.bachelorwork.ui.utils.extensions.toDp
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class OrderEditAddedProductModalBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentModalBottomSheetOrderManageProductBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetOrderManageProductBinding
        get() = FragmentModalBottomSheetOrderManageProductBinding::inflate

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    private val viewModel: OrderEditAddedProductViewModel by viewModels()

    private val sharedViewModel: OrderManageProductSharedViewModel by hiltViewModelNavigation(Destination.CreateOrder)

    private val adapter: SimpleBindingAdapter<OrderSelectableProductListUi> =
        simpleAdapter<OrderSelectableProductListUi, ProductItemInOrderSelectionBinding> {
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
                    binding.textInputLayoutQuantity.hint = getString(R.string.hint_quantity_unit, product.unit)
                    viewModel.selectItem(product.id)
                }
                checkboxDetail.onClick { product ->
                    viewModel.navigateToProductDetail(product.id)
                }
            }
        }


    override fun setupViews() {
        setupSearchBar()
        setupRecycleView()
        setupEditTextForm()
        setupEditButton()

        collectInLifecycle(viewModel.uiState) {
            setupUiState(it)
        }
        collectInLifecycle(viewModel.uiFormState) {
            setupFormUiState(it)
        }
        collectInLifecycle(viewModel.searchQuery) { query ->
            if (query.isEmpty()) {
                binding.textViewEnterOrScan.text = getString(R.string.text_enter_or_scan)
                binding.textViewEnterOrScan.visibility = View.VISIBLE
            } else {
                binding.textViewEnterOrScan.visibility = View.INVISIBLE
            }
        }

    }

    private fun setupUiState(uiState: OrderManageProductUiState) {
        val productIsSelected = if (uiState.isSelectedProduct) View.VISIBLE else View.GONE

        binding.textInputLayoutQuantity.visibility = productIsSelected
        binding.textInputLayoutRate.visibility = productIsSelected
        binding.buttonManageProduct.visibility = productIsSelected

        if (uiState.isLoading) {
            binding.recyclerViewProducts.visibility = View.INVISIBLE
            binding.textViewEnterOrScan.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerViewProducts.visibility = View.VISIBLE
        }

        if (uiState.products.isEmpty()) {
            if (viewModel.searchQuery.value.isEmpty()) {
                binding.textViewEnterOrScan.text = getString(R.string.text_enter_or_scan)
            } else {
                binding.textViewEnterOrScan.text = getString(R.string.text_no_result)
            }
            binding.textViewEnterOrScan.visibility = View.VISIBLE
        } else {
            binding.textViewEnterOrScan.visibility = View.INVISIBLE
        }

        adapter.submitList(uiState.products)
    }

    private fun setupFormUiState(uiState: OrderManageProductFormUiState) {
        binding.textInputLayoutQuantity.error = uiState.quantityError
        binding.textInputLayoutRate.error = uiState.rateError
    }

    private fun setupRecycleView() {
        with(binding.recyclerViewProducts) {
            setHasFixedSize(true)
            adapter = this@OrderEditAddedProductModalBottomSheetFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSearchBar() {
        binding.editTextSearch.doAfterTextChanged {
            viewModel.onSearchQueryChanged(it.toString())
        }
        binding.textInputLayoutSearch.setEndIconOnClickListener {
            viewModel.scanBarcodeScanner()
        }
    }

    private fun setupEditTextForm() {
        binding.editTextQuantity.doAfterTextChanged {
            viewModel.onEvent(OrderManageProductFormEvent.Quantity(it.toString()))
        }
        binding.editTextRate.doAfterTextChanged {
            viewModel.onEvent(OrderManageProductFormEvent.Rate(it.toString()))
        }
        binding.editTextRate.setText(String.format(Locale.getDefault(), "%.2f", viewModel.rate))
        binding.editTextQuantity.setText(String.format(Locale.getDefault(), "%.2f", viewModel.quantity))
    }

    private fun setupEditButton() {
        binding.buttonManageProduct.text = getString(R.string.text_button_edit_product)
        binding.buttonManageProduct.setOnClickListener {
            viewModel.getSelectedProductData()?.let {
                sharedViewModel.replaceProduct(it)
            }
        }
    }
}