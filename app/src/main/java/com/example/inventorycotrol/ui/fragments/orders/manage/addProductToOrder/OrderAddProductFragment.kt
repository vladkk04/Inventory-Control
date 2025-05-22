package com.example.inventorycotrol.ui.fragments.orders.manage.addProductToOrder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentOrderProductManageBinding
import com.example.inventorycotrol.databinding.OrderSearchableProductBinding
import com.example.inventorycotrol.domain.model.order.OrderProductSelectedData
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.fragments.orders.manage.OrderManageProductSharedViewModel
import com.example.inventorycotrol.ui.model.order.SelectableProductUi
import com.example.inventorycotrol.ui.model.order.product.OrderAddProductUiState
import com.example.inventorycotrol.ui.model.order.product.OrderManageProductFormEvent
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.hiltViewModelNavigation
import com.example.inventorycotrol.ui.utils.extensions.toDp
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderAddProductFragment : BaseBottomSheetDialogFragment<FragmentOrderProductManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderProductManageBinding
        get() = FragmentOrderProductManageBinding::inflate

    override val isAllowFullScreen: Boolean = false

    override val isFullScreen: Boolean = false

    private val sharedViewModel: OrderManageProductSharedViewModel by hiltViewModelNavigation(Destination.CreateOrder)

    private val viewModel: OrderAddProductViewModel by viewModels()

    private lateinit var linearLayoutManager: LinearLayoutManager

    private val adapter = simpleAdapter<SelectableProductUi, OrderSearchableProductBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.name == newItem.name }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.textViewName.text = item.name

            item.image?.let {
                Glide.with(requireContext())
                    .load("${AppConstants.BASE_URL_CLOUD_FRONT}${item.image}")
                    .placeholder(R.drawable.ic_image)
                    .fallback(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .centerCrop()
                    .into(imageView)
            }


            if(item.isSelected) {
                cardView.strokeWidth = 2.toDp(requireContext())
                layout.alpha = 0.5f
            } else {
                cardView.strokeWidth = 0
                layout.alpha = 1f
            }
        }
        listeners {
            root.onClick { product ->
                binding.textInputQuantity.hint = getString(R.string.hint_quantity_unit, product.unit)
                viewModel.selectItem(product.id)
            }
        }
    }

    override fun setupViews() {
        setupRecycleView()
        setupSearchBar()
        setupShowAllTextButton()
        setupEditTextForm()
        setupAddButton()

        collectInLifecycle(viewModel.uiState) {
            updateUiState(it)
        }
    }

    private fun updateUiState(uiState: OrderAddProductUiState) {

        binding.progressBar.isVisible = uiState.isLoading
        binding.textInputPrice.hint = getString(R.string.hint_price_value, uiState.currency)

        if (uiState.products.any { it.isSelected }) {
            showInputFields()
            binding.buttonAddProduct.isVisible = true
        } else {
            hideInputFields()
            binding.buttonAddProduct.isGone = true
        }

        if (uiState.pinnedProduct != null && uiState.products.size == 1 && uiState.isBarcode) {
            binding.textViewShowAll.isVisible = true
        } else {
            binding.textViewShowAll.isVisible = false
        }

        binding.buttonAddProduct.isEnabled = uiState.canAddProduct
        binding.textViewNoProducts.isVisible = uiState.products.isEmpty() && !uiState.isLoading

        adapter.submitList(uiState.products) {
            binding.recyclerView.visibility = if (uiState.isLoading) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun setupRecycleView() {
        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = this@OrderAddProductFragment.adapter
            layoutManager = linearLayoutManager
        }
    }

    private fun showInputFields() {
        binding.textInputQuantity.isVisible = true
        binding.textInputPrice.isVisible = true
    }

    private fun hideInputFields() {
        binding.textInputQuantity.isGone = true
        binding.textInputPrice.isGone = true
    }

    private fun setupSearchBar() {
        binding.textInputSearch.setEndIconOnClickListener {
            viewModel.scanBarcodeScanner()
        }
    }

    private fun setupEditTextForm() {
        binding.editTextQuantity.doAfterTextChanged {
            viewModel.onEvent(OrderManageProductFormEvent.Quantity(it.toString()))
        }
        binding.editTextPrice.doAfterTextChanged {
            viewModel.onEvent(OrderManageProductFormEvent.Price(it.toString()))
        }
        binding.editTextSearch.doAfterTextChanged {
            viewModel.onEvent(OrderManageProductFormEvent.Search(it.toString()))
        }
    }

    private fun setupShowAllTextButton() {
        binding.textViewShowAll.setOnClickListener {
            viewModel.showAllProducts()
        }
    }

    private fun setupAddButton() {
        binding.buttonAddProduct.setOnClickListener {
            sharedViewModel.addProduct(
                OrderProductSelectedData(
                    productSelectedId = viewModel.uiState.value.pinnedProduct!!.id,
                    quantity = binding.editTextQuantity.text.toString().toDouble(),
                    image = viewModel.uiState.value.pinnedProduct!!.image,
                    name = viewModel.uiState.value.pinnedProduct!!.name,
                    rate = binding.editTextPrice.text.toString().toDouble()
                )
            )
        }
    }
}