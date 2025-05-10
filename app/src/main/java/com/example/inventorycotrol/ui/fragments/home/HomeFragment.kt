package com.example.inventorycotrol.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.NotificationSettings
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings
import com.example.inventorycotrol.databinding.CriticalStockItemBinding
import com.example.inventorycotrol.databinding.FragmentHomeBinding
import com.example.inventorycotrol.databinding.ProductUpdateStockCompactItemBinding
import com.example.inventorycotrol.domain.model.organisation.OrganisationRole
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.ui.MainViewModel
import com.example.inventorycotrol.ui.model.home.FilterCriticalItems
import com.example.inventorycotrol.ui.model.home.HomeUiState
import com.example.inventorycotrol.ui.model.productUpdateStock.ProductUpdateStockCompact
import com.example.inventorycotrol.ui.utils.menu.createPopupMenu
import com.example.inventorycotrol.ui.views.CustomFloatingMenu
import com.example.inventorycotrol.ui.views.CustomFloatingUpdateStockMenu
import com.example.inventorycotrol.ui.worker.StockCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val mainViewModel: MainViewModel by activityViewModels()

    private val adapterCriticalProduct = simpleAdapter<Product, CriticalStockItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.textViewNameProduct.text = item.name
            this.textViewProductLeftCount.text =
                getString(R.string.text_quantity_unit_value, item.quantity, item.unit.name)
        }
    }

    private val adapterLastUpdateTransaction =
        simpleAdapter<ProductUpdateStockCompact, ProductUpdateStockCompactItemBinding> {
            areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
            areContentsSame = { oldItem, newItem -> oldItem == newItem }

            bind { item ->
                val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault())
                val date = formatter.format(Date(item.updateAt))


                this.totalUpdate.text = "${item.totalUpdate} items updated"
                this.updateAt.text = date
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getProfile()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (_binding == null) {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
        }

        viewModel.viewModelScope.launch() {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.organisationRole.collectLatest {
                    when (it) {
                        OrganisationRole.ADMIN -> {
                            setupFabAllAdminButton()
                        }

                        OrganisationRole.EMPLOYEE -> {
                            setupFabAllEmployeeButton()
                        }

                        null -> {
                            setupFabAllAdminButton()
                        }
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                updateUiState(uiState)
                uiState.notificationSettings?.let {
                    uiState.thresholdSettings?.let { set ->
                        scheduleStockCheckWorker(it, set)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mainViewModel.uiState.collectLatest { uiState ->
                binding.textViewToolbarTitle.text =
                    getString(R.string.text_hello_user, uiState.profile?.fullName ?: "Unknown Name")

                uiState.profile?.imageUrl?.let {
                    Glide.with(requireContext())
                        .load("${AppConstants.BASE_URL_CLOUD_FRONT}/${it}")
                        .error(R.drawable.ic_identity)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.profileCirclePicture.root)
                }
            }
        }

        binding.profileCirclePicture.root.setOnClickListener {
            viewModel.openDrawer()
        }

        setupPopupMenuCriticalItems()
        setupCriticalProductRecyclerView()
        setupLastUpdateTransactionRecyclerView()
        setupRefreshLayout()

        return binding.root
    }

    private fun setupPopupMenuCriticalItems() {
        binding.textViewCriticalStockShowLast.setOnClickListener {
            createPopupMenu(
                requireContext(),
                binding.textViewCriticalStockShowLast,
                R.menu.popup_critical_stocks_last
            ).apply {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.show_all -> {
                            viewModel.setupCriticalStockFilter(FilterCriticalItems.SHOW_ALL)
                            true
                        }

                        R.id.show_last_ten -> {
                            viewModel.setupCriticalStockFilter(FilterCriticalItems.LAST_10)
                            true
                        }

                        R.id.show_last_fifty -> {
                            viewModel.setupCriticalStockFilter(FilterCriticalItems.LAST_50)
                            true
                        }

                        R.id.show_last_hundred -> {
                            viewModel.setupCriticalStockFilter(FilterCriticalItems.LAST_100)
                            true
                        }

                        else -> false
                    }
                }
                setOnDismissListener {
                    binding.textViewCriticalStockShowLast.text = getString(
                        R.string.text_show_period_hide,
                        viewModel.uiState.value.filterCriticalItems.name.lowercase()
                            .replaceFirstChar(Char::titlecase)
                            .replace('_', ' ')
                    )
                }
            }.show()
            binding.textViewCriticalStockShowLast.text = getString(
                R.string.text_show_period_expand,
                viewModel.uiState.value.filterCriticalItems.name.lowercase()
                    .replaceFirstChar(Char::titlecase)
                    .replace('_', ' ')
            )
        }
    }

    private fun setupRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun setupFabAllEmployeeButton() {
        CustomFloatingUpdateStockMenu(requireContext(), binding.fabAll).apply {
            setOnUpdateStockClickListener {
                viewModel.navigateToUpdateStock()
            }
        }
    }

    private fun setupFabAllAdminButton() {
        CustomFloatingMenu(requireContext(), binding.fabAll).apply {
            setOnCreateProductClickListener {
                viewModel.navigateToCreateProduct()
            }
            setOnInviteUserClickListener {
                viewModel.navigateToInviteUser()
            }
            setOnCreateOrderClickListener {
                viewModel.navigateToCreateOrder()
            }
            setOnUpdateStockClickListener {
                viewModel.navigateToUpdateStock()
            }
        }
    }

    private fun setupCriticalProductRecyclerView() {
        with(binding.recyclerViewCriticalStock) {
            adapter = adapterCriticalProduct
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupLastUpdateTransactionRecyclerView() {
        with(binding.recyclerViewLastStockActivity) {
            adapter = adapterLastUpdateTransaction
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun updateUiState(uiState: HomeUiState) {
        binding.progressBarLastCriticalStock.isGone = !uiState.isLoading
        binding.progressBarLastStockActivity.isGone = !uiState.isLoading

        binding.textViewCriticalStockShowLast.text = getString(
            R.string.text_show_period_hide,
            uiState.filterCriticalItems.name.lowercase().replaceFirstChar(Char::titlecase)
                .replace('_', ' ')
        )

        binding.textViewNoActivities.isVisible =
            uiState.productUpdateStockItems.isEmpty() && !uiState.isLoading
        binding.textViewNoCriticalStocks.isVisible =
            uiState.criticalStockItems.isEmpty() && !uiState.isLoading
        binding.swipeRefresh.isRefreshing = uiState.isRefreshing && !uiState.isLoading
        binding.textViewTotalUserCount.text =
            String.format(Locale.getDefault(), "%d", uiState.totalUsersCount)
        binding.textViewTotalProductsCount.text =
            String.format(Locale.getDefault(), "%d", uiState.totalProductsCount)

        adapterLastUpdateTransaction.submitList(uiState.productUpdateStockItems)
        adapterCriticalProduct.submitList(uiState.criticalStockItems)
    }

    private fun scheduleStockCheckWorker(
        settings: NotificationSettings,
        thresholdSettings: ThresholdSettings
    ) {
        val workManager = WorkManager.getInstance(requireContext())

        if (!settings.notifiableRoles.contains(mainViewModel.organisationRole.value)) {
            workManager.cancelUniqueWork("stockCheck")
            return
        }

        workManager.cancelUniqueWork("stockCheck")

        val notificationTimeParts = settings.notificationTime.split(":")
        val hour = notificationTimeParts[0].toInt()
        val minute = notificationTimeParts.getOrElse(1) { "0" }.toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val initialDelay = calendar.timeInMillis - System.currentTimeMillis()

        val inputData = workDataOf(
            "critical_threshold_percentage" to thresholdSettings.criticalThresholdPercentage,
            "notification_days" to settings.notificationDays.toIntArray()
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = PeriodicWorkRequestBuilder<StockCheckWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "stockCheck",
            ExistingPeriodicWorkPolicy.UPDATE, request
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


}

