package com.example.bachelorwork.ui.fragments.warehouse.productManage.productEdit

import android.view.MenuItem
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.ui.fragments.warehouse.productManage.BaseProductManageFragment
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProductEditFragment : BaseProductManageFragment() {

    override val viewModel: ProductEditViewModel by viewModels()

    override val titleToolbar: String = "Edit product"

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                viewModel.updateProduct()
                true
            }

            else -> false
        }
    }

    override fun setupViews() {
        super.setupViews()

        viewLifecycleOwner.collectInLifecycle(viewModel.product) { uiState ->
            updateUIState(uiState)
        }
    }

    private fun updateUIState(product: Product?) {
        if(product != null) {
            binding.editTextName.setText(product.name)
            binding.editTextBarcode.setText(product.barcode)
            binding.editTextMinStockLevel.setText(String.format(Locale.getDefault(), "%.2f", product.minStockLevel))
            binding.editTextQuantity.setText(String.format(Locale.getDefault(), "%.2f", product.quantity))
            binding.editTextDescription.setText(product.description)
            binding.customInputCategories.setDefaultCategory(product.category)
            binding.customInputTags.addTags(*product.tags.toTypedArray())
            binding.numberPickerUnit.value = product.unit.ordinal
        }
    }
}


