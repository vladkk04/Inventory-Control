package com.example.inventorycotrol.ui.fragments.warehouse.productDetail.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentProductDetailTimelineHistoryBinding
import com.example.inventorycotrol.databinding.ProductDetailTimelineHistoryItemBinding
import com.example.inventorycotrol.domain.model.product.ProductTimelineHistory
import com.example.inventorycotrol.ui.fragments.warehouse.productDetail.ProductDetailViewModel
import com.example.inventorycotrol.ui.model.product.detail.ProductTimelineHistoryUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class ProductDetailTimelineHistoryFragment : Fragment() {

    private var _binding: FragmentProductDetailTimelineHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModelDetail: ProductDetailViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val adapter = simpleAdapter<ProductTimelineHistory, ProductDetailTimelineHistoryItemBinding> {
        val formatter = SimpleDateFormat(AppConstants.PRODUCT_TIMELINE_HISTORY_FORMAT, Locale.getDefault())

        bind { item ->
            when (item) {
                is ProductTimelineHistory.ProductTimelineTimelineCreate -> {
                    this.line.visibility = View.GONE
                    this.textViewHeader.text = getString(R.string.text_product_created)
                    this.textViewModify.text = getString(
                        R.string.text_product_timeline_history_modify,
                        formatter.format(Date.from(Instant.ofEpochMilli(item.createdAt)))
                    )
                    this.imageViewTimelineAbout.setImageResource(R.drawable.ic_edit)
                }
                is ProductTimelineHistory.ProductTimelineUpdate -> {
                    this.line.visibility = View.VISIBLE
                    this.textViewHeader.text = getString(R.string.text_product_updated)
                    this.textViewModify.text = getString(
                        R.string.text_product_timeline_history_modify,
                        formatter.format(Date.from(Instant.ofEpochMilli(item.updates.updatedAt)))
                    )
                    this.imageViewTimelineAbout.setImageResource(R.drawable.ic_update)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailTimelineHistoryBinding.inflate(inflater, container, false)
        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModelDetail.uiState.collectLatest { uiState ->
                uiState.product?.let { product ->
                    val create = ProductTimelineHistory.ProductTimelineTimelineCreate(
                        createdAt = product.createdAt,
                        createdBy = product.createdBy
                    )

                    val updates = product.updates.map {
                        ProductTimelineHistory.ProductTimelineUpdate(updates = it)
                    }.sortedByDescending { it.updates.updatedAt }

                    val updateHistory = updates + create
                    updateUI(ProductTimelineHistoryUiState(updateHistory = updateHistory))
                }
            }
        }


        return binding.root
    }

    private fun updateUI(uiState: ProductTimelineHistoryUiState) {
        adapter.submitList(uiState.updateHistory)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@ProductDetailTimelineHistoryFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }


}