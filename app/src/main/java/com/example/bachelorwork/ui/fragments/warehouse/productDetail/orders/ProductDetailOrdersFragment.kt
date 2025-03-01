package com.example.bachelorwork.ui.fragments.warehouse.productDetail.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductDetailOrdersBinding
import com.example.bachelorwork.databinding.ProductDetailOrderItemBinding
import com.example.bachelorwork.ui.constant.Constants
import com.example.bachelorwork.ui.model.product.detail.ProductDetailOrdersUiState
import com.example.bachelorwork.ui.model.product.detail.orders.ProductDetailOrderUi
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ProductDetailOrdersFragment : Fragment() {
    private var _binding: FragmentProductDetailOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailOrdersViewModel by viewModels({ requireParentFragment() })

    private val adapter = simpleAdapter<ProductDetailOrderUi, ProductDetailOrderItemBinding> {
        val formatter = SimpleDateFormat(
            Constants.DateFormats.PRODUCT_TIMELINE_HISTORY_FORMAT,
            Locale.getDefault()
        )

        bind { item ->
            this.textViewOrderId.text = getString(R.string.text_order_id, item.orderId)
            this.textViewOrderedDate.text = formatter.format(item.orderedAt)
            this.textViewOrderedProduct.text = getString(
                R.string.text_detail_ordered_product,
                item.name,
                item.quantity,
                item.unit.lowercase()
            )
            this.checkBoxNavigateOrder.setOnClickListener {
                viewModel.navigateToOrder(item.orderId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailOrdersBinding.inflate(inflater, container, false)
        setupRecyclerView()

        collectInLifecycle(viewModel.uiState) { uiState ->
            setupUiState(uiState)
        }

        return binding.root
    }

    private fun setupUiState(uiState: ProductDetailOrdersUiState) {
        binding.textViewNoOrders.visibility = if (uiState.orders.isEmpty()) View.VISIBLE else View.GONE


        adapter.submitList(uiState.orders)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@ProductDetailOrdersFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}