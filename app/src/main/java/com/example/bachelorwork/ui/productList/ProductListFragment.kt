package com.example.bachelorwork.ui.productList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductListBinding
import com.example.bachelorwork.domain.model.ProductUI
import com.example.bachelorwork.ui.productCreate.ProductCreateModalBottomSheet
import com.example.bachelorwork.ui.utils.createStateListDrawableChecked
import com.google.android.material.search.SearchView

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAdapter: ProductListAdapter
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)

        adaptLayoutToEdge()
        setupRecyclerView()

        setupSearchView()
        setupSearchViewMenuClickListener()

        setupSwipeRefreshLayoutListener()
        setupFabButtonsOnClickListener()
        setupCheckBoxChangeProductsViewTypeListener()

        return binding.root
    }

    private fun setupCheckBoxChangeProductsViewTypeListener() {
        binding.checkboxChangeViewProducts.apply {
            buttonDrawable = createStateListDrawableChecked(
                requireContext(),
                R.drawable.ic_view_grid,
                R.drawable.ic_view_row
            )
        }.setOnCheckedChangeListener { _, isChecked ->
            if(binding.recyclerViewProducts.itemAnimator?.isRunning == true) return@setOnCheckedChangeListener

            if (isChecked) {
                gridLayoutManager.spanCount = 2
                listAdapter.setViewType(ProductListAdapter.ProductViewType.GRID)
            } else {
                gridLayoutManager.spanCount = 1
                listAdapter.setViewType(ProductListAdapter.ProductViewType.ROW)
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

        //TODO delete this
        listAdapter = ProductListAdapter().apply {
            val listOfProduct: ArrayList<ProductUI> = ArrayList()
            for (i in 0..200) {
                val product = ProductUI(
                    name = "Wood",
                    upcCode = "234234",
                    //image = Uri.parse("android.resource://com.example.bachelorwork/drawable/ic_add"),
                    price = 20.0,
                    weight = 10.0,
                    quantity = 100
                )
                listOfProduct.add(product)
            }
            submitList(listOfProduct)
        }

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
            ProductCreateModalBottomSheet().also {
                it.show(parentFragmentManager, it.TAG)
            }
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

    private fun setupSearchView() {
        binding.searchViewProducts.addTransitionListener { _, _, newState ->
            val bottomNavigationBar = requireActivity().findViewById<View>(R.id.bottom_navigation)

            when (newState) {
                SearchView.TransitionState.HIDING -> bottomNavigationBar.visibility = View.VISIBLE
                SearchView.TransitionState.SHOWING -> bottomNavigationBar.visibility = View.GONE
                else -> return@addTransitionListener
            }
        }
    }

    private fun setupSearchViewMenuClickListener() {
        /*binding.searchBarProducts.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_product_filter -> {
                    true
                }
                else -> false
            }
        }*/
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}