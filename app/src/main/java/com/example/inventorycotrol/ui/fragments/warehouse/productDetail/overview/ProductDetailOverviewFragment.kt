package com.example.inventorycotrol.ui.fragments.warehouse.productDetail.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentProductDetailOverviewBinding
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.ui.fragments.warehouse.productDetail.ProductDetailViewModel
import com.example.inventorycotrol.ui.views.createProductTagChip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailOverviewFragment : Fragment() {

    private var _binding: FragmentProductDetailOverviewBinding? = null
    private val binding get() = _binding!!

    private val productDetailViewModel: ProductDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailOverviewBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            productDetailViewModel.uiState.collectLatest { uiState ->
                uiState.product?.let { updateUI(it) }
            }
        }
        return binding.root
    }

    private fun updateUI(product: Product) {
        with(product) {
            binding.textViewId.text = getString(R.string.text_product_id_value, id)
            binding.textViewCategory.text = getString(R.string.text_category_value, product.categoryName ?: "Unidentified")
            binding.textViewQuantity.text = getString(R.string.text_quantity_current_value, quantity, unit.name.lowercase())
            binding.textViewMinimumStockLevel.text = getString(R.string.text_product_item_min_stock_detail_value, minStockLevel, unit.name.lowercase())
            binding.textViewUnit.text = getString(R.string.text_measurement_unit_value, unit.name)
            binding.textViewDescription.text = description
            tags.forEach { tag -> binding.chipGroupTags.addView(createProductTagChip(requireContext(), tag)) }
        }
    }

}