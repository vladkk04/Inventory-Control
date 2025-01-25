package com.example.bachelorwork.ui.fragments.orders.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrderDetailBinding
import com.example.bachelorwork.databinding.ProductItemInOrderDetailBinding
import com.example.bachelorwork.domain.model.order.OrderProductSubItem
import com.example.bachelorwork.ui.constant.Constants
import com.example.bachelorwork.ui.dialogs.createDeleteDialog
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderDetailViewModel by viewModels()

    private val adapter = simpleAdapter<OrderProductSubItem, ProductItemInOrderDetailBinding> {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        collectInLifecycle(viewModel.uiState, Dispatchers.Main.immediate) {
            binding.toolbar.title = "Order #${it.order?.id}"
            binding.toolbar.subtitle = it.order?.orderedAt?.let { it1 ->
                SimpleDateFormat(Constants.DateFormats.PRODUCT_TIMELINE_HISTORY_FORMAT, Locale.getDefault()).format(
                    it1
                )
            }

            //adapter.submitList(it.order?.products)
        }

        setupToolbarMenu()
        setupRecyclerView()


        return binding.root
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
                    createDeleteDialog(requireContext(),
                        deleteItemTitle = "Order #${viewModel.uiState.value.order?.id}",
                        onPositiveButtonClick = {
                            viewModel.deleteOrder()
                        }
                    ).show()
                    true
                }

                R.id.edit -> {
                    true
                }

                else -> false
            }
        }
    }
}