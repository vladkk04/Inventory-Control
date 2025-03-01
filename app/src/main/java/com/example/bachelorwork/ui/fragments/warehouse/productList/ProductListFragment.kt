package com.example.bachelorwork.ui.fragments.warehouse.productList

import android.os.Bundle
import android.view.View
import android.widget.TextView
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
import com.example.bachelorwork.ui.model.product.list.ProductListUIState
import com.example.bachelorwork.ui.model.product.list.ProductSearchUIState
import com.example.bachelorwork.ui.utils.StateListDrawableFactory
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.extensions.viewBinding
import com.example.bachelorwork.ui.utils.menu.createPopupMenu
import com.example.bachelorwork.ui.utils.recyclerview.SpeedyLinearSmoothScroller
import com.example.bachelorwork.ui.utils.recyclerview.UpwardScrollButtonListener
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private val binding by viewBinding(FragmentProductListBinding::bind)

    private val viewModel: ProductListViewModel by viewModels()

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var listAdapter: ProductListAdapter
    private lateinit var searchListAdapter: ProductListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        collectInLifecycle(
            viewModel.searchUiState,
            lifecycleState = Lifecycle.State.STARTED
        ) { searchUiState ->
            updateSearchUIState(searchUiState)
        }
        collectInLifecycle(
            viewModel.uiState,
            lifecycleState = Lifecycle.State.STARTED
        ) { uiState ->
            updateUiState(uiState)
        }

        setupRecyclerView()
        setupSearchBar()

        setupOnBackPressCallback()
        setupSwipeRefreshLayoutListener()
        setupProfileButtonOnClickListener()
        setupCheckboxChangeViewTypeProductsListener()
        setupCheckboxOrderByProductsListener()

    }

    private fun setupCheckboxChangeViewTypeProductsListener() {
        binding.checkboxChangeDisplayTypeProducts.apply {
            buttonDrawable = StateListDrawableFactory.createCheckedDrawable(
                requireContext(),
                R.drawable.ic_view_grid,
                R.drawable.ic_view_row
            )
        }.setOnClickListener {
            if (binding.recyclerView.itemAnimator?.isRunning == true) return@setOnClickListener
            viewModel.changeProductViewDisplayType()
        }
    }

    private fun setupCheckboxOrderByProductsListener() {
        binding.checkBoxSortDirection.apply {
            buttonDrawable = StateListDrawableFactory.createCheckedDrawable(
                requireContext(),
                R.drawable.ic_arrow_up,
                R.drawable.ic_arrow_down
            )
        }.setOnClickListener {
            viewModel.changeSortDirection()
            binding.recyclerView.itemAnimator = null
        }

        binding.buttonSortBy.setOnClickListener {
            createPopupMenu(requireContext(), it, R.menu.popup_sort_by_product_menu).apply {
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.sort_by_name -> {
                            viewModel.getProductsSortedBy(SortBy.NAME)
                            true
                        }

                        R.id.sort_by_quantity -> {
                            viewModel.getProductsSortedBy(SortBy.QUANTITY)
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
            viewModel.uiState.value.viewDisplayType.ordinal + 1
        )

        binding.fabScrollUp.setOnClickListener {
            gridLayoutManager.startSmoothScroll(SpeedyLinearSmoothScroller(requireContext()))
        }

        listAdapter.setOnItemClickListener { id ->
            viewModel.navigateToItemDetail(id)
        }

        with(binding.recyclerView) {
            adapter = listAdapter
            layoutManager = gridLayoutManager
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
    }

    private fun setupSearchBar() {
        searchListAdapter = ProductListAdapter()

        with(binding.recyclerViewSearchProducts) {
            adapter = searchListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }

        searchListAdapter.setOnItemClickListener { id ->
            viewModel.navigateToItemDetail(id)
        }

        binding.searchView.editText.doAfterTextChanged {
            viewModel.searchProducts(it.toString())
        }

        binding.searchBar.menu.findItem(R.id.filters).actionView?.let { view ->
            view.setOnLongClickListener {
                filterPopupMenu(view).show()
                true
            }

            view.setOnClickListener {
                viewModel.navigateToFilters()
            }
        }
    }

    private fun filterPopupMenu(anchorView: View) =
        createPopupMenu(requireContext(), anchorView, R.menu.popup_filters_menu).apply {
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.clear -> {
                        viewModel.clearFilters()
                        true
                    }

                    else -> false
                }
            }
        }

    private fun setupOnBackPressCallback() {
        val callback =
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, false) {
                binding.searchView.hide()
            }

        binding.searchView.addTransitionListener { _, _, newState ->
            callback.isEnabled = newState == SearchView.TransitionState.SHOWN
        }
    }

    private fun setupSwipeRefreshLayoutListener() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
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

    private fun updateUiState(uiState: ProductListUIState) {
        if (uiState.isLoading) {
            binding.circleProgressIndicator.visibility = View.VISIBLE
        } else {
            binding.circleProgressIndicator.visibility = View.INVISIBLE
            binding.textViewNoItems.visibility = if (uiState.products.isEmpty()) View.VISIBLE else View.GONE

            binding.buttonSortBy.text =
                if (uiState.sortOptions.sortBy == SortBy.NAME) getString(R.string.title_name)
                else getString(R.string.title_quantity)

            uiState.viewDisplayType.apply {
                gridLayoutManager.spanCount = ordinal + 1
                listAdapter.setViewType(this)
            }

            val firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition()

            listAdapter.submitList(uiState.products) {
                val firstVisibleView = gridLayoutManager.findViewByPosition(firstVisiblePosition)

                val offset = firstVisibleView?.top ?: 0
                gridLayoutManager.scrollToPositionWithOffset(firstVisiblePosition, offset)
            }
        }

        binding.searchBar.menu.findItem(R.id.filters).actionView?.let { view ->
            val textViewFiltersCount = view.findViewById<TextView>(R.id.text_view_filters_count)

            if (uiState.filtersCount == 0) {
                textViewFiltersCount.visibility = View.GONE
            } else {
                view.findViewById<TextView>(R.id.text_view_filters_count).text = String.format(
                    Locale.getDefault(), "%d", uiState.filtersCount
                )
                textViewFiltersCount.visibility = View.VISIBLE
            }
        }
    }

    private fun updateSearchUIState(uiState: ProductSearchUIState) {
        binding.textViewSearchContentProducts.visibility =
            if (uiState.products.isEmpty()) View.VISIBLE else View.GONE

        searchListAdapter.submitList(uiState.products)
    }
}