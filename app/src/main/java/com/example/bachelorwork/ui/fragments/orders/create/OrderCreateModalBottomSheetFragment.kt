package com.example.bachelorwork.ui.fragments.orders.create

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetOrderManageBinding
import com.example.bachelorwork.databinding.ProductItemInOrderCreationBinding
import com.example.bachelorwork.domain.model.order.OrderAddedProduct
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.dialogs.createDiscardDialog
import com.example.bachelorwork.ui.model.order.create.DiscountType
import com.example.bachelorwork.ui.model.order.create.OrderCreateUiState
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.hiltViewModelNavigation
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


@AndroidEntryPoint
class OrderCreateModalBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentModalBottomSheetOrderManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetOrderManageBinding
        get() = FragmentModalBottomSheetOrderManageBinding::inflate

    override val titleToolbar: String = "Create Order"

    override val inflateMenu: Int
        get() = R.menu.bottom_sheet_modify_order_menu

    private val sharedViewModel: OrderManageProductSharedViewModel by hiltViewModelNavigation(Destination.CreateOrder)

    private val viewModel: OrderCreateViewModel by viewModels()

    override fun onNavigationIconToolbarClickListener() {
        createDiscardDialog(onPositiveButtonClick = { dismiss() }).show()
    }

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                viewModel.createOrder()
                true
            }

            else -> false
        }
    }

    private val adapter = simpleAdapter<OrderAddedProduct, ProductItemInOrderCreationBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind {
            this.textViewName.text = it.name
            this.textViewAmount.text = getString(
                R.string.text_amount_order_added_product,
                it.quantity,
                it.unit.name,
                it.rate
            )
            this.textViewTotal.text = String.format(Locale.getDefault(), "%.2f", it.total)

            this.checkboxRemove.isChecked = false
            this.checkboxEdit.isChecked = false
        }
        listeners {
            this.checkboxRemove.onClick { item ->
                viewModel.deleteAddedProduct(item)
            }
            this.checkboxEdit.onClick { item ->
                viewModel.navigateToOrderEditAddedProduct(item)
            }
        }
    }

    override fun setupViews() {
        setupRecyclerView()
        setupAddProductToOrderLayout()
        setupDiscountSelector()

        collectInLifecycle(sharedViewModel.selectedProduct, Lifecycle.State.STARTED) {
            viewModel.addProductToOrder(it)
        }

        collectInLifecycle(viewModel.uiState, Lifecycle.State.STARTED) {
            setupUiState(it)
        }
        collectInLifecycle(
            findNavController().currentBackStackEntry?.savedStateHandle
                ?.getStateFlow("discount", 0.00)
                ?: return
        ) {
            viewModel.setDiscount(it)
        }

    }

    private fun setupUiState(uiState: OrderCreateUiState) {
        if (uiState.addedProduct.isEmpty()) {
            binding.layoutAddProduct.visibility = View.VISIBLE
        } else {
            binding.layoutAddProduct.visibility = View.GONE
        }

        val discountType = when (uiState.discountType) {
            DiscountType.PERCENTAGE -> getString(
                R.string.text_discount_percentage,
                uiState.discount
            )
            DiscountType.FIXED -> getString(R.string.text_discount, uiState.discount)
        }


        binding.textviewSubtotal.text = getString(R.string.text_subtotal, uiState.subtotal)
        binding.textviewDiscount.text = discountType
        binding.textViewTotal.text = getString(R.string.text_total, uiState.total)

        adapter.submitList(uiState.addedProduct.toList())
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrderCreateModalBottomSheetFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupDiscountSelector() {
        binding.radioButtonPercentage.setOnClickListener {
            viewModel.navigateToOrderSetDiscount(DiscountType.PERCENTAGE)
        }

        binding.radioButtonFixed.setOnClickListener {
            viewModel.navigateToOrderSetDiscount(DiscountType.FIXED)
        }
    }

    private fun setupAddProductToOrderLayout() {
        binding.layoutAddProduct.setOnClickListener {
            viewModel.navigateToOrderAddProduct()
        }
        binding.buttonAddProduct.setOnClickListener {
            viewModel.navigateToOrderAddProduct()
        }
    }
}