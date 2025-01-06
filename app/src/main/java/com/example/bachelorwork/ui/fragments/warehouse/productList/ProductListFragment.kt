package com.example.bachelorwork.ui.fragments.warehouse.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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
import com.example.bachelorwork.ui.model.product.productList.ProductListUIState
import com.example.bachelorwork.ui.model.product.productList.ProductSearchUIState
import com.example.bachelorwork.ui.utils.StateListDrawableFactory
import com.example.bachelorwork.ui.utils.menu.createPopupMenu
import com.example.bachelorwork.ui.utils.recyclerview.UpwardScrollButtonListener
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var listAdapter: ProductListAdapter
    private lateinit var searchListAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        viewLifecycleOwner.collectInLifecycle(
            viewModel.searchUiState,
            lifecycleState = Lifecycle.State.CREATED
        ) { searchUiState ->
            updateSearchUIState(searchUiState)
        }

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
        setupProfileButtonOnClickListener()
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
            createPopupMenu(requireContext(), it, R.menu.popup_sort_by_products_menu).apply {
                setOnMenuItemClickListener { menuItem ->
                    binding.textViewOrderByProducts.text = menuItem.title
                    when (menuItem.itemId) {
                        R.id.sort_by_name -> {
                            viewModel.getProductsChangeSortBy(SortBy.NAME)
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
                    binding.fabScrollUp,
                    gridLayoutManager
                )
            )
        }

        listAdapter.setOnItemClickListener { position ->
            viewModel.navigateToItemDetail(position)
        }
    }

    private fun setupSearchBar() {
        searchListAdapter = ProductListAdapter()
        searchListAdapter.setOnItemClickListener { position ->
            viewModel.navigateToItemDetail(position)
        }

        with(binding.recyclerViewSearchProducts) {
            adapter = searchListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }

        binding.searchViewProducts.editText.doAfterTextChanged {
            viewModel.searchProducts(it.toString())
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, false) {
            binding.searchViewProducts.hide()
        }

        binding.searchViewProducts.addTransitionListener { v, _, newState ->
            callback.isEnabled = newState == SearchView.TransitionState.SHOWN
        }

    }

    private fun setupSwipeRefreshLayoutListener() {
        binding.swipeRefreshLayoutProducts.setOnRefreshListener {
            binding.swipeRefreshLayoutProducts.isRefreshing = false
        }
    }

    private fun setupProfileButtonOnClickListener() {
        binding.profileCirclePicture.root.setOnClickListener {
            viewModel.openNavigationDrawer()
        }
        binding.fabCreateItem.setOnClickListener {
            viewModel.navigateToCreateItem()
        }
    }

    private fun updateUIState(uiState: ProductListUIState) {
        val firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition()

        binding.textViewContentProducts.visibility = if (uiState.isNoProducts) View.VISIBLE else View.GONE

        gridLayoutManager.spanCount = uiState.viewType.ordinal + 1
        listAdapter.setViewType(uiState.viewType)
        listAdapter.submitList(uiState.products) {
            val firstVisibleView = gridLayoutManager.findViewByPosition(firstVisiblePosition)
            val offset = firstVisibleView?.top ?: 0
            gridLayoutManager.scrollToPositionWithOffset(firstVisiblePosition, offset)
        }
    }

    private fun updateSearchUIState(uiState: ProductSearchUIState) {
        binding.textViewSearchContentProducts.visibility = if(uiState.isNoItemsFound) View.VISIBLE else View.GONE
        searchListAdapter.submitList(uiState.products)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}