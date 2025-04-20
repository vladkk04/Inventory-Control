package com.example.bachelorwork.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.CriticalStockItemBinding
import com.example.bachelorwork.databinding.FragmentHomeBinding
import com.example.bachelorwork.databinding.ProductUpdateStockCompactItemBinding
import com.example.bachelorwork.domain.model.product.Product
import com.example.bachelorwork.ui.fragments.productUpdateStock.StockOperationType
import com.example.bachelorwork.ui.model.home.HomeUiState
import com.example.bachelorwork.ui.model.productUpdateStock.ProductUpdateStockCompact
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.ui.utils.screen.InsetHandler
import com.example.bachelorwork.ui.views.CustomFloatingUpdateStockMenu
import com.example.bachelorwork.ui.worker.StockCheckWorker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private val adapterCriticalProduct = simpleAdapter<Product, CriticalStockItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.textViewNameProduct.text = item.name
            this.textViewProductLeftCount.text = getString(R.string.text_quantity_unit_value, item.quantity, item.unit.name)
        }
    }

    private val adapterLastUpdateTransaction = simpleAdapter<ProductUpdateStockCompact, ProductUpdateStockCompactItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.operationType.text = item.operationType.name.replaceFirstChar(Char::titlecase).replace("_", " ")

            val drawable = when (item.operationType) {
                StockOperationType.STOCK_IN -> R.drawable.ic_arrow_upward_stock
                StockOperationType.STOCK_OUT -> R.drawable.ic_arrow_downward_stock
            }
            this.operationType.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(requireContext(), drawable), null)

            val formatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm", Locale.getDefault())
            val date = formatter.format(Date(item.updateAt))


            this.totalUpdate.text = "${item.totalUpdate} items updated"
            this.updateBy.text = getString(R.string.text_updated_by_with_circle, item.updateBy)
            this.updateAt.text = date
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        InsetHandler.adaptToEdgeWithMargin(binding.root)

        scheduleStockCheckWorker()

        collectInLifecycle(viewModel.uiState, Lifecycle.State.STARTED) { uiState ->
            

            binding.textViewNoActivities.isVisible = uiState.productUpdateStockItems.isEmpty()
            binding.textViewNoCriticalStocks.isVisible = uiState.criticalStockItems.isEmpty()
            binding.swipeRefresh.isRefreshing = uiState.isRefreshing && !uiState.isLoading
            binding.textViewTotalUserCount.text = String.format(Locale.getDefault(), "%d", uiState.totalUsersCount)
            binding.textViewTotalProductCount.text = String.format(Locale.getDefault(), "%d", uiState.totalProductsCount)

            adapterLastUpdateTransaction.submitList(uiState.productUpdateStockItems)
            adapterCriticalProduct.submitList(uiState.criticalStockItems)
        }


        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }

        setupCriticalProductRecyclerView()
        setupLastUpdateTransactionRecyclerView()

        setupFabAllButton()

        return binding.root
    }

    private fun setupFabAllButton() {
        CustomFloatingUpdateStockMenu(requireContext(), binding.fabAll).apply {
            setOnStockInClickListener {
                viewModel.navigateToStockIn()
            }
            setOnStockOutClickListener {
                viewModel.navigateToStockOut()
            }
        }


        /*CustomFloatingMenu(requireContext(), binding.fabAll).apply {
            setOnCreateProductClickListener {
                viewModel.navigateToCreateProduct()
            }
            setOnInviteUserClickListener {
                viewModel.navigateToInviteUser()
            }
            setOnCreateOrderClickListener {
                viewModel.navigateToCreateOrder()
            }
        }*/
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

    }

    private fun scheduleStockCheckWorker() {

        val workManager = WorkManager.getInstance(requireContext())

        workManager.cancelUniqueWork("stockCheck")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<StockCheckWorker>()
            .setInitialDelay(Duration.ofSeconds(10))
            .setBackoffCriteria(backoffPolicy = BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
            .setConstraints(constraints).build()

        workManager.enqueueUniqueWork("stockCheck", ExistingWorkPolicy.KEEP, request)
    }


}

