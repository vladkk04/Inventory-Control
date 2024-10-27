package com.example.bachelorwork.ui.fragments.productList

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductListBinding
import com.example.bachelorwork.ui.common.base.showPopupMenu
import com.example.bachelorwork.ui.common.extensions.createStateListDrawableChecked
import com.example.bachelorwork.ui.fragments.productCreate.ProductCreateModalBottomSheetFragment
import com.example.bachelorwork.ui.model.productList.ProductListUIState
import com.example.bachelorwork.ui.model.productList.toProductListUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: ProductListAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        adaptLayoutToEdge()
        setupRecyclerView()

        setupSwipeRefreshLayoutListener()
        setupFabButtonsOnClickListener()
        setupCheckboxChangeViewTypeProductsListener()
        setupCheckboxSortByProductsListener()

        viewModel.viewModelScope.launch {
            observeUiState()
        }

        return binding.root
    }

    private fun setupCheckboxChangeViewTypeProductsListener() {
        binding.checkboxChangeViewTypeProducts.apply {
            buttonDrawable = StateListDrawable().createStateListDrawableChecked(
                requireContext(),
                R.drawable.ic_view_grid,
                R.drawable.ic_view_row
            )
        }.setOnCheckedChangeListener { _, isChecked ->
            if (binding.recyclerViewProducts.itemAnimator?.isRunning == true) return@setOnCheckedChangeListener

            if (isChecked) {
                gridLayoutManager.spanCount = 2
                listAdapter.setViewType(ProductListAdapter.ProductViewType.GRID)
            } else {
                gridLayoutManager.spanCount = 1
                listAdapter.setViewType(ProductListAdapter.ProductViewType.ROW)
            }
        }
    }

    private fun setupCheckboxSortByProductsListener() {
        binding.checkboxSortByProducts.apply {
            buttonDrawable = StateListDrawable().createStateListDrawableChecked(
                requireContext(),
                R.drawable.ic_arrow_up,
                R.drawable.ic_arrow_down
            )
        }

        binding.textViewSortByProducts.setOnClickListener {
            showPopupMenu(it, R.menu.popup_sort_by_products_menu) { menuItem ->
                when(menuItem.itemId) {
                    R.id.sort_by_name -> {}
                    R.id.sort_by_price -> {}
                }
            }
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
        gridLayoutManager = GridLayoutManager(requireContext(), 1)

        with(binding.recyclerViewProducts) {
            adapter = listAdapter
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            addOnScrollListener(onScrollRecyclerViewListener())
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
            binding.recyclerViewProducts.smoothScrollToPosition(0)
        }
    }

    private fun onScrollRecyclerViewListener(): OnScrollListener {
        val slideUpAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up)
        val slideDownAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down)

        return object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val firstVisibleItemPosition = gridLayoutManager.findFirstVisibleItemPosition()
                val fabVisibility = binding.fabScrollUpProducts.visibility

                if ((firstVisibleItemPosition < 1 || dy > 0) && fabVisibility == View.VISIBLE) {
                    binding.fabScrollUpProducts.startAnimation(slideDownAnimation)
                    binding.fabScrollUpProducts.visibility = View.INVISIBLE
                } else if (dy < 0 && firstVisibleItemPosition > 1 && fabVisibility == View.INVISIBLE) {
                    binding.fabScrollUpProducts.startAnimation(slideUpAnimation)
                    binding.fabScrollUpProducts.visibility = View.VISIBLE
                }
            }
        }
    }

    private suspend fun observeUiState() = viewModel.uiState.collectLatest { uiState ->
        updateUiState(uiState)
    }

    private fun updateUiState(uiState: ProductListUIState) {
        listAdapter.submitList(uiState.productList.toProductListUI())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}