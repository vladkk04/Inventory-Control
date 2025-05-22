package com.example.inventorycotrol.ui.fragments.productUpdateStock

import android.graphics.Color
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentProductUpdateStockBinding
import com.example.inventorycotrol.databinding.ProductUpdateStockItemBinding
import com.example.inventorycotrol.domain.model.product.ProductUnit
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.model.productUpdateStock.ProductUpdateStockItem
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProductUpdateStockFragment :
    BaseBottomSheetDialogFragment<FragmentProductUpdateStockBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductUpdateStockBinding
        get() = FragmentProductUpdateStockBinding::inflate

    private val viewModel: ProductUpdateStockViewModel by viewModels()

    override val titleToolbar: String = "Update Stock"

    override fun onNavigationIconToolbarClickListener() {
        AppDialogs.createDiscardDialog(requireContext()) { dismiss() }.show()
    }

    private val productAdapter =
        simpleAdapter<ProductUpdateStockItem, ProductUpdateStockItemBinding> {
            areItemsSame = { old, new -> old.id == new.id }
            areContentsSame = { old, new -> old == new }

            bind { item ->
                when (item.unit) {
                    ProductUnit.PCS, ProductUnit.BOX -> {
                        editTextQuantity.inputType =
                            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL

                        this.editTextQuantity.setText(
                            String.format(
                                Locale.getDefault(),
                                "%d",
                                item.currentInputValue.toInt()
                            )
                        )
                    }

                    else -> {
                        editTextQuantity.inputType =
                            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

                        this.editTextQuantity.setText(
                            String.format(
                                Locale.getDefault(),
                                "%.2f",
                                item.currentInputValue
                            )
                        )
                    }
                }
                this.textViewName.text = item.name
                this.textViewStockOnHand.text = getString(
                    R.string.text_stock_value_unit,
                    item.stockOnHand,
                    item.unit.name.lowercase()
                )

                editTextQuantity.doAfterTextChanged {
                    editTextQuantity.setSelection(editTextQuantity.text?.length ?: 0)
                    buttonMinus.isEnabled = it.toString().toDoubleOrNull() != 0.00
                    buttonMinus.alpha = if (buttonMinus.isEnabled) 1f else 0.5f

                    viewModel.updateInputValue(item.id, editTextQuantity.text.toString().toDoubleOrNull())
                }
            }

            listeners {
                buttonAdd.onClick { item ->
                    viewModel.adjustStock(item.id, isAddition = true)
                }
                buttonMinus.onClick { item ->
                    viewModel.adjustStock(item.id, isAddition = false)
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
                searchView?.findViewById<View>(androidx.appcompat.R.id.search_plate)
                    ?.setBackgroundColor(Color.TRANSPARENT)

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
            else -> false
        }
    }

    override fun setupViews() {
        setupProductAdapter()

        collectInLifecycle(viewModel.uiState) {
            updateUiState(it)
        }

        binding.buttonUpdateStock.setOnClickListener {
            viewModel.updateStock()
        }
    }

    private fun updateUiState(uiState: ProductUpdateStockUiState) {
        productAdapter.submitList(uiState.products)
        showProgress(uiState.isLoading)

        binding.textViewNoProducts.isGone = uiState.isLoading || uiState.products.isNotEmpty()
        binding.progressBar.isGone = !uiState.isLoading
    }

    private fun setupProductAdapter() {
        with(binding.recyclerViewProducts) {
            adapter = productAdapter
            itemAnimator = null
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}