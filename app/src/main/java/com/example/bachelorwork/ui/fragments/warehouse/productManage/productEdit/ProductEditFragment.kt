package com.example.bachelorwork.ui.fragments.warehouse.productManage.productEdit

import android.view.MenuItem
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bachelorwork.R
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.ui.fragments.warehouse.productManage.BaseProductManageFragment
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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

        binding.textInputQuantity.isGone = true
        binding.numberPickerUnit.isGone = true
        binding.textViewUnit.isGone = true

        viewLifecycleOwner.collectInLifecycle(viewModel.product, Dispatchers.Main.immediate) { uiState ->
            updateUIState(uiState)
        }
    }

    private fun updateUIState(product: Product?) {
        if(product != null) {
            Glide.with(requireContext())
                .load("http://192.168.68.60:8080/${product.imageUrl}")
                .placeholder(R.drawable.ic_image)
                .fallback(R.drawable.ic_image)
                .centerCrop()
                .error(R.drawable.ic_image)
                .into(binding.imageView)

            binding.editTextName.setText(product.name)
            binding.editTextBarcode.setText(product.barcode)
            binding.editTextMinStockLevel.setText(String.format(Locale.getDefault(), "%.2f", product.minStockLevel))
            binding.editTextQuantity.setText(String.format(Locale.getDefault(), "%.2f", product.quantity))
            binding.editTextDescription.setText(product.description)
            product.categoryName?.let { binding.customInputCategories.setDefaultCategory(it) }
            binding.customInputTags.addTags(*product.tags.toTypedArray())
            binding.numberPickerUnit.value = product.unit.ordinal
        }
    }
}


