package com.example.bachelorwork.ui.fragments.productList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductListBinding
import com.example.bachelorwork.domain.model.product.SortBy
import com.example.bachelorwork.domain.model.product.toProductListUI
import com.example.bachelorwork.ui.common.StateListDrawableFactory
import com.example.bachelorwork.ui.fragments.productCreate.ProductCreateModalBottomSheetFragment
import com.example.bachelorwork.ui.model.productList.ProductListUIState
import com.example.bachelorwork.ui.utils.menu.createPopupMenu
import com.example.bachelorwork.ui.utils.recyclerview.SpeedyLinearSmoothScroller
import com.example.bachelorwork.ui.utils.recyclerview.UpwardScrollButtonListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductListViewModel by viewModels()

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var listAdapter: ProductListAdapter

    private var isSubmitRecyclerViewList = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            observeUiState()
        }

        adaptLayoutToEdge()
        setupRecyclerView()

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

    private fun adaptLayoutToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.statusBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            (v.layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(
                    bars.left,
                    bars.top,
                    bars.right,
                    bars.bottom
                )
                v.layoutParams = this
            }

            WindowInsetsCompat.CONSUMED
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

    }

    private fun setupSwipeRefreshLayoutListener() {
        binding.swipeRefreshLayoutProducts.setOnRefreshListener {
            binding.swipeRefreshLayoutProducts.isRefreshing = false
        }
    }

    private fun setupFabButtonsOnClickListener() {
        binding.fabCreateProduct.setOnClickListener {
            ProductCreateModalBottomSheetFragment().also { it.show(parentFragmentManager, it.TAG) }
        }
        binding.fabScrollUpProducts.setOnClickListener {
            binding.recyclerViewProducts.layoutManager?.startSmoothScroll(
                SpeedyLinearSmoothScroller(requireContext())
            )
        }
    }


    private suspend fun observeUiState() =
        viewModel.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .collectLatest { uiState -> updateUIState(uiState) }

    private fun updateUIState(uiState: ProductListUIState) {
        val firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition()

        gridLayoutManager.spanCount = uiState.viewType.ordinal + 1
        listAdapter.setViewType(uiState.viewType)
        listAdapter.submitList(uiState.products.toProductListUI()) {
            if (!isSubmitRecyclerViewList) {
                binding.recyclerViewProducts.startLayoutAnimation()
                isSubmitRecyclerViewList = true
            }
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