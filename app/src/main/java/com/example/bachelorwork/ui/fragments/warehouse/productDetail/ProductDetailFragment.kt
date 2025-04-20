package com.example.bachelorwork.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductDetailBinding
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.common.adapters.ViewPagerAdapter
import com.example.bachelorwork.ui.common.adapters.ViewPagerFragmentData
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.analytics.ProductDetailAnalyticsFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.overview.ProductDetailOverviewFragment
import com.example.bachelorwork.ui.fragments.warehouse.productDetail.timeline.ProductDetailTimelineHistoryFragment
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
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
    ): CoordinatorLayout {

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        setupViewPagerWithTabLayout()
        setupAppBarLayoutAnimation()
        setupToolbarMenuOnClickListener()

        viewLifecycleOwner.collectInLifecycle(viewModel.product) { product ->
            product?.let {
                updateUI(it)
            }
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
                        AppDialogs.createDeleteDialog(requireContext()) {
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

    private fun updateUI(product: Product) {
        Glide.with(requireContext())
            .load("http://192.168.68.60:8080/${product.imageUrl}")
            .placeholder(R.drawable.ic_image)
            .fallback(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .into(binding.imageViewToolbarProduct)

        Glide.with(requireContext())
            .load("http://192.168.68.60:8080/${product.imageUrl}")
            .placeholder(R.drawable.ic_image)
            .fallback(R.drawable.ic_image)
            .error(R.drawable.ic_image)
            .into(binding.imageViewProduct)


        binding.textViewToolbarProductName.text = product.name
        binding.textViewProductName.text = product.name
    }
}