package com.example.inventorycotrol.ui.fragments.warehouse.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentProductListBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.product.SortBy
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.fragments.warehouse.filters.SharedWarehouseFilterViewModel
import com.example.inventorycotrol.ui.model.product.list.ProductListUIState
import com.example.inventorycotrol.ui.model.product.list.ProductSearchUIState
import com.example.inventorycotrol.ui.utils.StateListDrawableFactory
import com.example.inventorycotrol.ui.utils.menu.createPopupMenu
import com.example.inventorycotrol.ui.utils.recyclerview.SpeedyLinearSmoothScroller
import com.example.inventorycotrol.ui.utils.recyclerview.UpwardScrollButtonListener
import com.example.inventorycotrol.ui.utils.screen.InsetHandler
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()

    private val sharedFilterViewModel: SharedWarehouseFilterViewModel by activityViewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var listAdapter: ProductListAdapter
    private lateinit var searchListAdapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentProductListBinding.inflate(inflater, container, false)
        }
        InsetHandler.adaptToEdgeWithMargin(binding.root)

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.isConnected.collectLatest {
                binding.fabCreateItem.isEnabled = it
            }
        }

        when (mainViewModel.organisationRole.value) {
            OrganisationRole.ADMIN -> {
                binding.fabCreateItem.visibility = View.VISIBLE
            }
            OrganisationRole.EMPLOYEE -> {
                binding.fabCreateItem.visibility = View.GONE
            }
            null -> {}
        }

        setupRecyclerView()
        setupSearchBar()

        setupOnBackPressCallback()
        setupSwipeRefreshLayoutListener()
        setupProfileButtonOnClickListener()
        setupCheckboxChangeViewTypeProductsListener()
        setupCheckboxOrderByProductsListener()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                updateUiState(uiState)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.thresholdSettings.collectLatest { settings ->
                    settings.let {
                        listAdapter.setupThresholdSettings(it)
                        searchListAdapter.setupThresholdSettings(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchUiState.collectLatest { uiState ->
                updateSearchUIState(uiState)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            sharedFilterViewModel.uiState.collectLatest { uiState ->
                viewModel.applyFilters(uiState)
            }
        }

        return binding.root
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
                        sharedFilterViewModel.clearFilters()
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
            viewModel.refreshProducts()
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
        binding.swipeRefresh.isRefreshing = uiState.isRefreshing

        uiState.imageUrl?.let {
            Glide.with(requireActivity())
                .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it}")
                .error(R.drawable.ic_identity)
                .into(binding.profileCirclePicture.root)
        }

        //binding.fabCreateItem.isEnabled = uiState.isEnableCreateButton

        if (uiState.isLoading && !uiState.isRefreshing) {
            binding.circleProgressIndicator.visibility = View.VISIBLE
            binding.textViewNoItems.visibility = View.GONE
        } else {
            binding.circleProgressIndicator.visibility = View.INVISIBLE
            binding.textViewNoItems.visibility = if (uiState.products.isEmpty() && !uiState.isLoading) View.VISIBLE else View.GONE

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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}