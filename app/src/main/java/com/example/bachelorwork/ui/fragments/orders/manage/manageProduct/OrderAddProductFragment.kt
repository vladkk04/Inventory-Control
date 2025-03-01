package com.example.bachelorwork.ui.fragments.orders.manage.manageProduct

import androidx.fragment.app.viewModels
import com.example.bachelorwork.ui.model.order.product.OrderManageProductUiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderAddProductFragment : BaseOrderProductManageFragment() {

    private val viewModel: OrderAddProductViewModel by viewModels()


    override fun setupViews() {
        /*setupSearchBar()
        setupRecycleView()
        setupEditTextForm()
        setupAddButton()

        collectInLifecycle(viewModel.uiStateForm, dispatcher = Dispatchers.Main.immediate) {
            setupUiState(it)
        }
        collectInLifecycle(viewModel.uiFormState) {
            setupFormUiState(it)
        }
        collectInLifecycle(viewModel.searchQuery) { query ->
            if (query.isEmpty()) {
                binding.textViewEnterOrScan.text = getString(R.string.text_enter_or_scan)
                binding.textViewEnterOrScan.visibility = View.VISIBLE
            } else {
                binding.textViewEnterOrScan.visibility = View.INVISIBLE
            }
        }*/
    }


    private fun setupUiState(uiState: OrderManageProductUiState) {
        /*val productIsSelected = if (uiStateForm.isSelectedProduct) View.VISIBLE else View.GONE

        binding.textInputQuantity.visibility = productIsSelected
        binding.textInputRate.visibility = productIsSelected
        binding.buttonManageProduct.visibility = productIsSelected

        if (uiStateForm.isLoading) {
            binding.recyclerView.visibility = View.INVISIBLE
            binding.textViewEnterOrScan.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        if (uiStateForm.products.isEmpty()) {
            if (viewModel.searchQuery.value.isEmpty()) {
                binding.textViewEnterOrScan.text = getString(R.string.text_enter_or_scan)
            } else {
                binding.textViewEnterOrScan.text = getString(R.string.text_no_result)
            }
            binding.textViewEnterOrScan.visibility = View.VISIBLE
        } else {
            binding.textViewEnterOrScan.visibility = View.INVISIBLE
        }

        adapter.submitList(uiStateForm.products)*/
    }






}