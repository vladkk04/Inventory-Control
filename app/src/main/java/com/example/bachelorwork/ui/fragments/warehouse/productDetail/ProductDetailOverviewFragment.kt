package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductDetailOverviewBinding
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.model.product.productDetail.ProductDetailUIState
import com.example.bachelorwork.ui.views.createProductTagChip

class ProductDetailOverviewFragment : Fragment() {

    private var _binding: FragmentProductDetailOverviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailOverviewBinding.inflate(inflater, container, false)

        viewLifecycleOwner.collectInLifecycle(viewModel.uiState) { uiState ->
            Log.d("tag", uiState.toString())
            updateUI(uiState)
        }

        return binding.root
    }

    private fun updateUI(uiState: ProductDetailUIState) {
        uiState.product?.let {
            with(it) {
                binding.textViewProductId.text = getString(R.string.text_product_id, it.id.toString())
                binding.textViewCategory.text = getString(R.string.text_category, category.name)
                binding.textViewQuantity.text = getString(R.string.text_quantity, quantity)
                binding.textViewMinimumStockLevel.text = getString(R.string.text_minimum_stock_level, minStockLevel)
                binding.textViewMeasurementUnit.text = getString(R.string.text_measurement_unit, unit.name)
                binding.textViewDescription.text = description
                tags.forEach { tag ->
                    binding.chipGroupTags.addView(createProductTagChip(requireContext(), tag))
                }
            }
        }
    }

}