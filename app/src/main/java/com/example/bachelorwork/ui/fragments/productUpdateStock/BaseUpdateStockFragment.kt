package com.example.bachelorwork.ui.fragments.productUpdateStock

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductUpdateStockBinding
import com.example.bachelorwork.databinding.ProductSearchableUpdateStockItemBinding
import com.example.bachelorwork.databinding.ProductUpdateStockItemBinding
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.order.SelectableProductUi
import com.example.bachelorwork.ui.model.productUpdateStock.StockAdjustmentUi
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.toDp

abstract class BaseUpdateStockFragment :
    BaseBottomSheetDialogFragment<FragmentProductUpdateStockBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductUpdateStockBinding
        get() = FragmentProductUpdateStockBinding::inflate

    protected val viewModel: BaseProductUpdateStockViewModel by viewModels()

    abstract val inputQuantityHint: String

    abstract val operationType: StockOperationType

    override fun onNavigationIconToolbarClickListener() {
        AppDialogs.createDiscardDialog(requireContext()) { dismiss() }.show()
    }

    private val productAdapter =
        simpleAdapter<SelectableProductUi, ProductSearchableUpdateStockItemBinding> {
            areItemsSame = { old, new -> old.id == new.id }
            areContentsSame = { old, new -> old == new }

            bind { item ->

                Glide.with(requireContext())
                    .load("http://192.168.68.60:8080/${item.image}")
                    .placeholder(R.drawable.ic_image)
                    .fallback(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .centerCrop()
                    .into(imageView)

                if (item.isSelected) {
                    cardView.strokeWidth = 2.toDp(requireContext())
                    layout.alpha = 0.5f
                } else {
                    cardView.strokeWidth = 0
                    layout.alpha = 1f
                }

                val color = when {
                    item.currentStock >= item.minStockLevel -> R.color.colorMinStockLevelNormalLevel
                    item.currentStock >= item.minStockLevel * 0.25 -> R.color.colorMinStockLevelMediumLevel
                    else -> R.color.colorMinStockLevelLowLevel
                }

                card.setBackgroundColor(ContextCompat.getColor(requireContext(), color))

                this.textViewName.text = item.name
                this.textViewQuantity.text = getString(
                    R.string.text_stock_value_unit,
                    item.currentStock,
                    item.unit.name.lowercase()
                )
            }

            listeners {
                root.onClick { item ->
                    viewModel.selectProduct(item)
                }
            }
        }

    private val productAdjustmentAdapter =
        simpleAdapter<StockAdjustmentUi, ProductUpdateStockItemBinding> {
            areItemsSame = { old, new -> old.productName == new.productName }
            areContentsSame = { old, new -> old == new }

            bind { item ->
                this.textViewName.text = item.productName
                this.textViewStockOnHand.text = getString(
                    R.string.text_stock_on_hand_value_unit,
                    item.currentStock,
                    item.unit.lowercase()
                )

                Glide.with(requireContext())
                    .load("http://192.168.68.60:8080/${item.imageUrl}")
                    .placeholder(R.drawable.ic_image)
                    .fallback(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .centerCrop()
                    .into(imageView)


                val adjustAmount = when (operationType) {
                    StockOperationType.STOCK_IN -> item.adjustedAmount + item.currentStock
                    StockOperationType.STOCK_OUT -> + item.currentStock - item.adjustedAmount
                }

                this.textViewNewQuantityOnHand.text = getString(
                    R.string.text_new_quantity_on_hand_value,
                    adjustAmount,
                    item.unit.lowercase()
                )
                val adjustUnit = when (operationType) {
                    StockOperationType.STOCK_IN -> "+" + item.adjustedAmount
                    StockOperationType.STOCK_OUT -> "-" + item.adjustedAmount
                }

                this.textViewQuantityAdjustedTotal.text = getString(
                    R.string.text_quantity_adjust_unit_value,
                    adjustUnit,
                    item.unit.lowercase()
                )
            }

            listeners {
                checkBoxRemove.onClick { item ->
                    viewModel.removeAdjustment(item)
                }
            }
        }

    override val inflateMenu: Int
        get() = R.menu.toolbar_product_update_stock

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.search -> {
                val searchView = menuItem.actionView as? SearchView
                searchView?.queryHint = "Search"
                searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)?.setBackgroundColor(Color.TRANSPARENT)

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.searchQuery(query)
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.searchQuery(newText)
                        return true
                    }
                })

                true
            }


            R.id.save -> {
                viewModel.updateStock()
                true
            }

            else -> false
        }
    }

    override fun setupViews() {
        setupProductAdapter()
        setupProductAddedAdapter()

        collectInLifecycle(viewModel.uiState) {
            updateUiState(it)
        }

        viewModel.setOperationType(operationType)

        binding.editTextQuantity.doAfterTextChanged {
            viewModel.setEnteredQuantity(it.toString())
        }



        binding.textInputQuantity.hint = inputQuantityHint

        binding.buttonAddProduct.setOnClickListener {
            viewModel.addAdjustment()
        }
    }

    private fun updateUiState(uiState: ProductUpdateStockUiState) {
        productAdapter.submitList(uiState.products)
        
        binding.progressBar.isGone = !uiState.isLoading
        binding.textViewNoProducts.isGone = !uiState.isLoading && uiState.products.isNotEmpty()
        binding.buttonAddProduct.isEnabled = uiState.canAddAdjustment
        productAdjustmentAdapter.submitList(uiState.adjustments)
    }

    private fun setupProductAdapter() {
        with(binding.recyclerViewProducts) {
            adapter = productAdapter
            itemAnimator = null
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupProductAddedAdapter() {
        with(binding.recyclerViewAddedProducts) {
            adapter = productAdjustmentAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


}