package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductDetailBinding
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.adapters.ViewPagerAdapter
import com.example.bachelorwork.ui.common.adapters.ViewPagerFragmentData
import com.example.bachelorwork.ui.fragments.warehouse.ProductDetailAnalyticFragment
import com.example.bachelorwork.ui.model.productDetail.ProductDetailUIState
import com.example.bachelorwork.ui.utils.dialogs.createDeleteDialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val viewModel: ProductDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        setupViewPagerWithTabLayout()
        setupAppBarLayoutAnimation()
        setupToolbarMenuOnClickListener()

        viewLifecycleOwner.collectInLifecycle(viewModel.uiState) { uiState ->
            updateUI(uiState)
        }

        return binding.root
    }

    private fun setupToolbarMenuOnClickListener() {
        binding.toolbarProductDetail.apply {
            setNavigationOnClickListener {
                viewModel.navigateToWarehouse()
            }
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.product_edit -> {
                        viewModel.editProduct()
                        true
                    }

                    R.id.product_delete -> {
                        createDeleteDialog(requireContext(), viewModel.uiState.value.product?.name ?: "") {
                            viewModel.deleteProduct()
                        }.show()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setupViewPagerWithTabLayout() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        viewPagerAdapter.addFragments(
            ViewPagerFragmentData(
                ProductDetailOverviewFragment(),
                "Overview",
                R.drawable.ic_description_outline
            ),
            ViewPagerFragmentData(
                ProductDetailAnalyticFragment(),
                "Analytics",
                R.drawable.ic_analytics_outline
            ),
            ViewPagerFragmentData(
                ProductDetailAnalyticFragment(),
                "History",
                R.drawable.ic_history
            ),
        )

        binding.viewPagerProductDetail.adapter = viewPagerAdapter
        binding.viewPagerProductDetail.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(
            binding.tabLayoutProductDetail,
            binding.viewPagerProductDetail
        ) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
            tab.setIcon(viewPagerAdapter.getIcon(position) ?: 0)
            binding.viewPagerProductDetail.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun setupAppBarLayoutAnimation() {
        binding.appbarLayoutDetailProduct.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val progress = abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)

            binding.collapsedToolbarProductName.alpha =
                abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)
            binding.textViewProductName.alpha = 1 - progress

            if (progress > 0) {
                binding.viewPagerProductDetail.translationY =
                    (1 - progress) * binding.textViewProductName.height
            }
        }
    }

    private fun updateUI(uiState: ProductDetailUIState) {
        binding.titleToolbarProductName.text = uiState.product?.name
        binding.textViewProductName.text = uiState.product?.name
    }
}