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
import com.example.bachelorwork.databinding.OrderItemOuterBinding
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.ui.utils.StateListDrawableFactory
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

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

            recyclerViewSubitems.visibility = View.GONE

            textViewOrderId.text =
                binding.root.context.getString(R.string.text_order_id, item.id)
            textViewOrderedDate.text =
                binding.root.context.getString(R.string.text_order_date_timestamp, item.date)
            textViewTotal.text =
                binding.root.context.getString(R.string.text_order_total, item.total)

            checkBoxToggle.apply {
                buttonDrawable = StateListDrawableFactory.createCheckedDrawable(
                    binding.root.context,
                    R.drawable.ic_keyboard_arrow_up,
                    R.drawable.ic_keyboard_arrow_down
                )
                setOnCheckedChangeListener { _, isChecked ->
                    recyclerViewSubitems.visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
            }

            /*recyclerViewSubitems.apply {
                adapterOuter = OrderSubItemAdapter().apply {
                    submitList(item.items)
                }
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(binding.root.context)
            }*/
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        setupRecyclerView()
        setupFabButton()

        return binding.root
    }

    private fun setupRecyclerView() {
        with(binding.recyclerViewOrders) {
            adapter = this@OrderListFragment.adapterOuter
            layoutManager = LinearLayoutManager(requireContext())
            setItemViewCacheSize(10)
            setHasFixedSize(true)
        }
    }

    private fun setupFabButton() {
        binding.fabCreateOrder.setOnClickListener {
            viewModel.navigateToCreateOrder()
        }
    }
}