package com.example.bachelorwork.ui.fragments.warehouse.productDetail.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.databinding.FragmentProductDetailTimelineHistoryBinding
import com.example.bachelorwork.databinding.ProductDetailTimelineHistoryItemBinding
import com.example.bachelorwork.domain.model.product.ProductTimelineHistory
import com.example.bachelorwork.ui.model.product.detail.ProductTimelineHistoryUiState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import java.text.SimpleDateFormat
import java.util.Locale

class ProductDetailTimelineHistoryFragment : Fragment() {

    private var _binding: FragmentProductDetailTimelineHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductDetailTimelineHistoryViewModel by viewModels(ownerProducer = { requireParentFragment() })

    private val adapter = simpleAdapter<ProductTimelineHistory, ProductDetailTimelineHistoryItemBinding> {
        val formatter = SimpleDateFormat(AppConstants.PRODUCT_TIMELINE_HISTORY_FORMAT, Locale.getDefault())

        bind { item ->
            when (item) {
                is ProductTimelineHistory.ProductTimelineTimelineCreate -> {
                    if (viewModel.uiState.value.updateHistory.lastIndex == index()) {
                        this.line.visibility = View.GONE
                    } else {
                        this.line.visibility = View.VISIBLE
                    }
                    this.textViewHeader.text = getString(R.string.text_product_created)
                    this.textViewModify.text = getString(
                        R.string.text_product_timeline_history_modify,
                        item.createdBy,
                        formatter.format(item.createdAt)
                    )
                    this.imageViewTimelineAbout.setImageResource(R.drawable.ic_edit)
                }
                is ProductTimelineHistory.ProductTimelineUpdate -> {
                    if (viewModel.uiState.value.updateHistory.lastIndex == index()) {
                        this.line.visibility = View.GONE
                    } else {
                        this.line.visibility = View.VISIBLE
                    }
                    this.textViewHeader.text = getString(R.string.text_product_updated)
                    this.textViewModify.text = getString(
                        R.string.text_product_timeline_history_modify,
                        item.updates.updatedBy,
                        formatter.format(item.updates.updatedAt)
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

        collectInLifecycle(viewModel.uiState) { uiState ->
            updateUI(uiState)
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