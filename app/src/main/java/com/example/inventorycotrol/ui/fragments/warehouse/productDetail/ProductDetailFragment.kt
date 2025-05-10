package com.example.inventorycotrol.ui.fragments.warehouse.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentProductDetailBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.common.adapters.ViewPagerAdapter
import com.example.inventorycotrol.ui.common.adapters.ViewPagerFragmentData
import com.example.inventorycotrol.ui.fragments.warehouse.productDetail.analytics.ProductDetailAnalyticsFragment
import com.example.inventorycotrol.ui.fragments.warehouse.productDetail.overview.ProductDetailOverviewFragment
import com.example.inventorycotrol.ui.fragments.warehouse.productDetail.timeline.ProductDetailTimelineHistoryFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val viewModel: ProductDetailViewModel by viewModels()

    private val mainActivityViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): CoordinatorLayout {

        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        when (mainActivityViewModel.organisationRole.value) {
            OrganisationRole.ADMIN -> {
                //binding.toolbar.menu.removeItem(R.id.product_edit)
            }
            OrganisationRole.EMPLOYEE -> {
                binding.toolbar.menu.removeItem(R.id.product_edit)
                binding.toolbar.menu.removeItem(R.id.product_delete)
            }
            null -> {

            }
        }


        setupViewPagerWithTabLayout()
        setupAppBarLayoutAnimation()
        setupToolbarMenuOnClickListener()



        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.product.collectLatest { product ->
                    product?.let {
                        updateUI(it)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                binding.progressBar.isVisible = isLoading
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
                binding.viewPager.translationY = 0 - (progress * binding.textViewProductName.height)
            }
        }
    }

    private fun updateUI(product: Product) {
        product.imageUrl?.let {
            Glide.with(requireContext())
                .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it}")
                .placeholder(R.drawable.ic_image)
                .fallback(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .apply {
                    into(binding.imageViewToolbarProduct)
                    into(binding.imageViewProduct)
                }
        }

        binding.textViewToolbarProductName.text = product.name
        binding.textViewProductName.text = product.name
    }
}