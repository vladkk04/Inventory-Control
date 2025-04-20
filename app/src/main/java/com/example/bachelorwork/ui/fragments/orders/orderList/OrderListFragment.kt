package com.example.bachelorwork.ui.fragments.orders.orderList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.data.constants.AppConstants
import com.example.bachelorwork.databinding.FragmentOrderListBinding
import com.example.bachelorwork.databinding.OrderItemBinding
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.ui.model.order.list.OrderListUiState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderListViewModel by viewModels()

    private val adapter = simpleAdapter<Order, OrderItemBinding> {
        bind { order ->
            root.setOnClickListener {
                viewModel.navigateToOrderDetail(order.id)
            }
            checkBoxNavigate.setOnClickListener {
                viewModel.navigateToOrderDetail(order.id)
            }

            textViewId.text = binding.root.context.getString(R.string.text_order_id, order.id)

            val formattedDate = Instant.ofEpochMilli(order.orderedAt)
                .atZone(ZoneId.systemDefault())  // Add timezone context
                .format(
                    DateTimeFormatter.ofPattern(AppConstants.ORDER_DATE_FORMAT)
                        .withLocale(Locale.getDefault())
                )
            textViewDate.text =
                binding.root.context.getString(
                    R.string.text_order_date_timestamp,
                    formattedDate
                )

            textViewTotal.text =
                binding.root.context.getString(R.string.text_order_total, order.total)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrderListBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        collectInLifecycle(viewModel.uiState, Dispatchers.Main.immediate) {
            setupUiState(it)
        }

        setupRecyclerView()
        setupFabButton()
        setupProfileButton()

        return binding.root
    }

    private fun setupUiState(uiState: OrderListUiState) {
        binding.textViewNoOrders.isVisible = uiState.orders.isEmpty()
        adapter.submitList(uiState.orders)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrderListFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setItemViewCacheSize(10)
            setHasFixedSize(true)
        }
    }

    private fun setupProfileButton() {
        binding.profileCirclePicture.root.setOnClickListener {
            viewModel.openDrawer()
        }
    }

    private fun setupFabButton() {
        binding.fabCreateOrder.setOnClickListener {
            viewModel.navigateToCreateOrder()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}