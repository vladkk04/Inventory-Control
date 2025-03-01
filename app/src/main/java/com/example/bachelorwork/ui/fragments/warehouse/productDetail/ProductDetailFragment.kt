package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductDetailBinding
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.common.adapters.ViewPagerAdapter
import com.example.bachelorwork.ui.common.adapters.ViewPagerFragmentData
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.analytics.ProductDetailAnalyticsFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.orders.ProductDetailOrdersFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.overview.ProductDetailOverviewFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.overview.ProductDetailOverviewViewModel
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.timeline.ProductDetailTimelineHistoryFragment
import com.example.bachelorwork.ui.model.product.detail.ProductDetailUIState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val viewModel: ProductDetailOverviewViewModel by viewModels()

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
        binding.toolbar.apply {
            setNavigationOnClickListener {
                viewModel.navigateBack()
            }
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.product_edit -> {
                        viewModel.editProduct()
                        true
                    }

                    R.id.product_delete -> {
                        AppDialogs.createDeleteDialog(requireContext(), viewModel.uiState.value.product?.name ?: "") {
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
                ProductDetailOrdersFragment(),
                "Orders",
                R.drawable.ic_list_outlined
            ),
            ViewPagerFragmentData(
                ProductDetailAnalyticsFragment(),
                "Analytics",
                R.drawable.ic_analytics_outline
            ),
            ViewPagerFragmentData(
                ProductDetailTimelineHistoryFragment(),
                "History",
                R.drawable.ic_history
            ),
        )

        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            tab.text = viewPagerAdapter.getTitle(position)
            tab.setIcon(viewPagerAdapter.getIcon(position) ?: 0)
            binding.viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

    private fun setupAppBarLayoutAnimation() {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val progress = abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)

            binding.customToolbarLayoutTitle.alpha =
                abs(verticalOffset.toFloat() / appBarLayout.totalScrollRange)
            binding.textViewProductName.alpha = 1 - progress

            if (progress > 0) {
                binding.viewPager.translationY =
                    (1 - progress) * binding.textViewProductName.height
            }
        }
    }

    private fun updateUI(uiState: ProductDetailUIState) {
        binding.textViewToolbarProductName.text = uiState.product?.name
        binding.textViewProductName.text = uiState.product?.name
    }
}