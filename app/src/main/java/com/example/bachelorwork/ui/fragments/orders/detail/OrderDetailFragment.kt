package com.example.bachelorwork.ui.fragments.orders.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrderDetailBinding
import com.example.bachelorwork.databinding.OrderDetailProductItemBinding
import com.example.bachelorwork.domain.model.order.OrderProductSubItem
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.constant.Constants
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class OrderDetailFragment : Fragment(R.layout.fragment_order_detail) {
    private val binding by viewBinding(FragmentOrderDetailBinding::bind)

    private val viewModel: OrderDetailViewModel by viewModels()

    private val adapter = simpleAdapter<OrderProductSubItem, OrderDetailProductItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.textViewName.text = item.name
            this.textViewQuantity.text = String.format(Locale.getDefault(), "%.2f", item.quantity)
            this.textViewPrice.text = String.format(Locale.getDefault(), "%.2f", item.price)
        }
        listeners {
            root.setOnClickListener {
                //viewModel.navigateToProductDetail(item.productId)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        collectInLifecycle(viewModel.uiState, Dispatchers.Main.immediate) {
            binding.toolbar.title = "Order #${it.order?.id}"
            binding.toolbar.subtitle = it.order?.orderedAt?.let { it1 ->
                SimpleDateFormat(
                    Constants.DateFormats.PRODUCT_TIMELINE_HISTORY_FORMAT,
                    Locale.getDefault()
                ).format(
                    it1
                )
            }
            binding.textViewTotalPrice.text = String.format(Locale.getDefault(), "%.2f", it.order?.total)
           /* when (it.discountType) {
                *//*DiscountType.PERCENTAGE ->  binding.textViewDiscountPrice.text = "${it.discount} %"
                DiscountType.FIXED -> binding.textViewDiscountPrice.text = "${it.discount}"
                null -> {""}*//*
            }*/

            binding.textViewSubtotalPrice.text = String.format(Locale.getDefault(), "%.2f", it.subTotal)

            adapter.submitList(it.order?.products)
        }

        setupToolbarMenu()
        setupRecyclerView()
    }


    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrderDetailFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupToolbarMenu() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.navigateUp()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    AppDialogs.createDeleteDialog(
                        requireContext(),
                        deleteItemTitle = "Order #${viewModel.uiState.value.order?.id}",
                    ) { viewModel.deleteOrder() }.show()
                    true
                }

                R.id.edit -> {
                    viewModel.navigateToOrderEdit()
                    true
                }

                else -> false
            }
        }
    }
}