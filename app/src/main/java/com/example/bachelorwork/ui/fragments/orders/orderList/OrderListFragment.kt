package com.example.bachelorwork.ui.fragments.orders.orderList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrderListBinding
import com.example.bachelorwork.databinding.OrderItemInnerBinding
import com.example.bachelorwork.databinding.OrderItemOuterBinding
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.domain.model.order.OrderProductSubItem
import com.example.bachelorwork.ui.constant.Constants
import com.example.bachelorwork.ui.model.order.OrderListUiState
import com.example.bachelorwork.ui.utils.StateListDrawableFactory
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderListViewModel by viewModels()

    private val adapterOuter = simpleAdapter<Order, OrderItemOuterBinding> {
        bind { item ->
            root.setOnClickListener {
                viewModel.navigateToOrderDetail(item.id)
            }

            textViewOrderId.text =
                binding.root.context.getString(R.string.text_order_id, item.id)
            textViewOrderedDate.text =
                binding.root.context.getString(
                    R.string.text_order_date_timestamp,
                    SimpleDateFormat(
                        Constants.DateFormats.PRODUCT_TIMELINE_HISTORY_FORMAT,
                        Locale.getDefault()
                    ).format(item.orderedAt)
                )
            textViewTotal.text =
                binding.root.context.getString(R.string.text_order_total, item.total)

            checkBoxToggle.apply {
                buttonDrawable = StateListDrawableFactory.createCheckedDrawable(
                    binding.root.context,
                    R.drawable.ic_keyboard_arrow_up,
                    R.drawable.ic_keyboard_arrow_down
                )
                setOnCheckedChangeListener { _, isChecked ->
                    recyclerViewSubitems.visibility = if (isChecked) View.VISIBLE else View.GONE
                }
            }

            val adapterInner = simpleAdapter<OrderProductSubItem, OrderItemInnerBinding> {
                bind { item ->
                    this.textViewName.text = item.name
                    this.textViewPrice.text = getString(R.string.text_price, item.price)
                    this.textViewQuantity.text = getString(R.string.text_order_subitem_quantity, item.quantity, item.unit.lowercase())
                }
            }

            recyclerViewSubitems.apply {
                adapter = adapterInner
                layoutManager = LinearLayoutManager(binding.root.context)
                setHasFixedSize(true)
            }

            //adapterInner.submitList(item.products)
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
        adapterOuter.submitList(uiState.orders)
    }

    private fun setupRecyclerView() {
        with(binding.recyclerViewOrders) {
            adapter = this@OrderListFragment.adapterOuter
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
}