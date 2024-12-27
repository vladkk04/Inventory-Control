package com.example.bachelorwork.ui.fragments.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachelorwork.databinding.FragmentManageOrdersBinding
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.domain.model.order.OrderSubItemProduct
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManageOrdersFragment : Fragment() {

    private var _binding: FragmentManageOrdersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var adapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageOrdersBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        setupRecyclerView()


        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = OrdersAdapter()
        adapter.submitList(List(10) {
            Order(
                items = List(10) { OrderSubItemProduct("Apple", 1.00, 1)  },
                total = 20.00
            )
        })
        with(binding.recyclerViewOrders) {
            adapter = this@ManageOrdersFragment.adapter
            layoutManager = object : LinearLayoutManager(requireContext()) {
                override fun canScrollVertically(): Boolean = false
            }
            setItemViewCacheSize(10)
            setHasFixedSize(true)
        }
    }
}