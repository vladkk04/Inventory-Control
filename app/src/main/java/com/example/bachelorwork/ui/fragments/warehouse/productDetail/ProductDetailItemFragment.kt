package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductDetailItemBinding
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.constant.Constants
import com.example.bachelorwork.ui.model.productDetail.ProductDetailUIState
import com.example.bachelorwork.ui.views.createProductTagChip
import java.text.SimpleDateFormat
import java.util.Locale

class ProductDetailItemFragment : Fragment() {

    private var _binding: FragmentProductDetailItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailItemBinding.inflate(inflater, container, false)

        viewLifecycleOwner.collectInLifecycle(viewModel.uiState) { uiState ->
            updateUI(uiState)
        }

        return binding.root
    }

    private fun updateUI(uiState: ProductDetailUIState) {
        uiState.product?.let {
            with(it) {
                binding.textViewProductId.text = getString(R.string.text_product_id, it.id.toString())
                binding.textViewCategory.text = getString(R.string.text_category, category.name)
                binding.textViewUnitPrice.text = getString(R.string.text_unit_price, productUnit.name)
                binding.textViewTotalPrice.text = getString(R.string.text_total_price, totalPrice)
                binding.textViewQuantity.text = getString(R.string.text_quantity, quantity)
                binding.textViewMinimumStockLevel.text = getString(R.string.text_minimum_stock_level, minStockLevel)
                binding.textViewPurchaseDate.text = getString(
                    R.string.text_purchase_date, SimpleDateFormat(
                        Constants.DEFAULT_DATE_FORMAT_PATTERN, Locale.getDefault()
                    ).format(datePurchase)
                )
                binding.textViewMeasurementUnit.text = getString(R.string.text_measurement_unit, productUnit.name)
                tags.forEach { tag ->
                    binding.chipGroupTags.addView(createProductTagChip(requireContext(), tag))
                }
                binding.textViewDescription.text = description
            }
        }
    }

}