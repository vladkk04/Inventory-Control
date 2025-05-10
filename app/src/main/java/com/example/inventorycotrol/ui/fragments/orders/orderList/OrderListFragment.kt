package com.example.inventorycotrol.ui.fragments.orders.orderList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentOrderListBinding
import com.example.inventorycotrol.databinding.OrderItemBinding
import com.example.inventorycotrol.domain.model.order.Order
import com.example.inventorycotrol.ui.model.order.list.OrderListUiState
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

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

            textViewTotalValue.text = String.format(Locale.getDefault(), "%.2f\u00A0%s", order.total, viewModel.uiState.value.currency)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrderListBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewModel.uiState.collectLatest {
                setupUiState(it)
            }
        }

        setupRecyclerView()
        setupFabButton()
        setupProfileButton()
        setupSwipeRefreshLayout()

        return binding.root
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupUiState(uiState: OrderListUiState) {

        binding.progressBar.isVisible = uiState.isLoading && !uiState.isRefreshing
        binding.swipeRefresh.isRefreshing = uiState.isRefreshing

        uiState.imageUrl?.let {
            Glide.with(requireActivity())
                .load("${AppConstants.BASE_URL_CLOUD_FRONT}/${it}")
                .error(R.drawable.ic_identity)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.profileCirclePicture.root)
        }


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