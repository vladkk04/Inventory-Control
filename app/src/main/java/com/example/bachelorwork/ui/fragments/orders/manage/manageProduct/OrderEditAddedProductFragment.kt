package com.example.bachelorwork.ui.fragments.orders.manage.manageProduct

import androidx.fragment.app.viewModels
import com.example.bachelorwork.ui.model.order.product.OrderManageProductUiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderEditAddedProductFragment : BaseOrderProductManageFragment() {

    private val viewModel: OrderEditAddedProductViewModel by viewModels()

   /* private val adapter: SimpleBindingAdapter<OrderSearchableProductListUi> =
        simpleAdapter<OrderSearchableProductListUi, OrderCreateSearchableProductBinding> {
            areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
            areContentsSame =
                { oldItem, newItem -> oldItem == newItem && index(oldItem) == index(newItem) }

            bind { item ->
                this.textViewName.text = item.name

                if (item.isSelected) {
                    this.layout.alpha = 0.5f
                    this.cardView.strokeWidth = 2.toDp(requireContext())
                } else {
                    this.layout.alpha = 1f
                    this.cardView.strokeWidth = 0
                }
            }
            listeners {
                root.onClick { product ->
                    binding.textInputQuantity.hint = getString(R.string.hint_quantity_unit, product.unit)
                    viewModel.selectItem(product.id)
                }
                checkBoxDetail.onClick { product ->
                    viewModel.navigateToProductDetail(product.id)
                }
            }
        }*/


    override fun setupViews() {
       /* setupSearchBar()
        setupRecycleView()
        setupEditTextForm()
        setupEditButton()

        collectInLifecycle(viewModel.uiStateForm) {
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