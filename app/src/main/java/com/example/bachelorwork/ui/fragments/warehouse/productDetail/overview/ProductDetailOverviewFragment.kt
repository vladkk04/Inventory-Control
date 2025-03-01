package com.example.bachelorwork.ui.fragments.warehouse.productDetail.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductDetailOverviewBinding
import com.example.bachelorwork.ui.model.product.detail.ProductDetailUIState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.views.createProductTagChip

class ProductDetailOverviewFragment : Fragment() {

    private var _binding: FragmentProductDetailOverviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailOverviewViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailOverviewBinding.inflate(inflater, container, false)

        viewLifecycleOwner.collectInLifecycle(viewModel.uiState) { uiState ->
            updateUI(uiState)
        }

        return binding.root
    }

    private fun updateUI(uiState: ProductDetailUIState) {
        uiState.product?.let {
            with(it) {
                binding.textViewId.text = getString(R.string.text_product_id_value, it.id.toString())
                binding.textViewCategory.text = getString(R.string.text_category_value, category?.name ?: "Unidentified")
                binding.textViewQuantity.text = getString(R.string.text_quantity_value, quantity)
                binding.textViewMinimumStockLevel.text = getString(R.string.text_product_item_min_stock_value, minStockLevel)
                binding.textViewUnit.text = getString(R.string.text_measurement_unit_value, unit.name)
                binding.textViewDescription.text = description
                tags.forEach { tag ->
                    binding.chipGroupTags.addView(createProductTagChip(requireContext(), tag))
                }
            }
        }
    }

}