package com.example.bachelorwork.ui.fragments.orderList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachelorwork.databinding.FragmentOrderListBinding
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.domain.model.order.OrderSubItemProduct
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderListViewModel by viewModels()
    private lateinit var adapter: OrderListAdapter

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
        adapter = OrderListAdapter()
        adapter.submitList(List(10) {
            Order(
                items = List(10) { OrderSubItemProduct("Apple", (10 .. 299).random().toDouble(), (10 .. 299).random())  },
                total = 20.00
            )
        })
        adapter.setOnItemClickListener { orderId ->
            viewModel.navigateToOrderDetail(orderId)
        }
        with(binding.recyclerViewOrders) {
            adapter = this@OrderListFragment.adapter
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