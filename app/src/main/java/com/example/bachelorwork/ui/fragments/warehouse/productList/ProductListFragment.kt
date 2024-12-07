package com.example.bachelorwork.ui.fragments.warehouse.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductListBinding
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.StateListDrawableFactory
import com.example.bachelorwork.ui.model.productList.ProductListUIState
import com.example.bachelorwork.ui.utils.menu.createPopupMenu
import com.example.bachelorwork.ui.utils.recyclerview.SpeedyLinearSmoothScroller
import com.example.bachelorwork.ui.utils.recyclerview.UpwardScrollButtonListener
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var listAdapter: ProductListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        viewLifecycleOwner.collectInLifecycle(
            viewModel.uiState,
            lifecycleState = Lifecycle.State.CREATED
        ) { uiState ->
            updateUIState(uiState)
        }

        InsetHandler.adaptToEdgeWithMargin(binding.root)
        setupRecyclerView()
        setupSearchBar()

        setupSwipeRefreshLayoutListener()
        setupFabButtonsOnClickListener()
        setupCheckboxChangeViewTypeProductsListener()
        setupCheckboxOrderByProductsListener()

        return binding.root
    }

    private fun setupCheckboxChangeViewTypeProductsListener() {
        binding.checkboxChangeViewTypeProducts.apply {
            buttonDrawable = StateListDrawableFactory.createCheckedDrawable(
                requireContext(),
                R.drawable.ic_view_grid,
                R.drawable.ic_view_row
            )
        }.setOnClickListener {
            if (binding.recyclerViewProducts.itemAnimator?.isRunning == true) return@setOnClickListener
            viewModel.changeViewType()
        }
    }

    private fun setupCheckboxOrderByProductsListener() {
        binding.checkboxOrderTypeProducts.apply {
            buttonDrawable = StateListDrawableFactory.createCheckedDrawable(
                requireContext(),
                R.drawable.ic_arrow_up,
                R.drawable.ic_arrow_down
            )
        }.setOnClickListener {
            viewModel.getProductsChangeSortDirection()
            binding.recyclerViewProducts.itemAnimator = null
        }

        binding.textViewOrderByProducts.setOnClickListener {
            createPopupMenu(it, R.menu.popup_sort_by_products_menu).apply {
                setOnMenuItemClickListener { menuItem ->
                    binding.textViewOrderByProducts.text = menuItem.title
                    when (menuItem.itemId) {
                        R.id.sort_by_name -> {
                            viewModel.getProductsChangeSortBy(SortBy.NAME)
                            true
                        }

                        R.id.sort_by_price -> {
                            viewModel.getProductsChangeSortBy(SortBy.PRICE)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }
    }

    private fun setupRecyclerView() {
        listAdapter = ProductListAdapter()

        gridLayoutManager = GridLayoutManager(
            requireContext(),
            viewModel.uiState.value.viewType.ordinal + 1
        )

        with(binding.recyclerViewProducts) {
            adapter = listAdapter
            layoutManager = gridLayoutManager
            itemAnimator = null
            setHasFixedSize(true)
            setItemViewCacheSize(10)
            addOnScrollListener(
                UpwardScrollButtonListener(
                    requireContext(),
                    binding.fabScrollUpProducts,
                    gridLayoutManager
                )
            )
        }
        listAdapter.setOnItemClickListener { position ->
            viewModel.navigateToItemDetail(position)
        }
    }

    private fun setupSearchBar() {
        binding.searchViewProducts.editText.doAfterTextChanged {
            viewModel.searchProducts(it.toString())
        }
        with(binding.recyclerViewSearchProducts) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
            setHasFixedSize(true)
            setItemViewCacheSize(10)
        }
    }

    private fun setupSwipeRefreshLayoutListener() {
        binding.swipeRefreshLayoutProducts.setOnRefreshListener {
            binding.swipeRefreshLayoutProducts.isRefreshing = false
        }
    }

    private fun setupFabButtonsOnClickListener() {
        binding.fabCreateProduct.setOnClickListener {
            viewModel.navigateToCreateProduct()
        }
        binding.fabScrollUpProducts.setOnClickListener {
            binding.recyclerViewProducts.layoutManager?.startSmoothScroll(
                SpeedyLinearSmoothScroller(requireContext())
            )
        }
    }

    private fun updateUIState(uiState: ProductListUIState) {

        val firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition()

        val visibility = if (uiState.products.isEmpty()) View.VISIBLE else View.GONE

        binding.textViewSearchContentProducts.visibility = visibility
        binding.textViewContentProducts.visibility = visibility

        gridLayoutManager.spanCount = uiState.viewType.ordinal + 1
        listAdapter.setViewType(uiState.viewType)
        listAdapter.submitList(uiState.products) {
            val firstVisibleView = gridLayoutManager.findViewByPosition(firstVisiblePosition)
            val offset = firstVisibleView?.top ?: 0
            gridLayoutManager.scrollToPositionWithOffset(firstVisiblePosition, offset)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}