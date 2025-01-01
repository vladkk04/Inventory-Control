package com.example.bachelorwork.ui.fragments.orderList.create

import android.graphics.Color
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.SimpleBindingAdapter
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetOrderAddProductBinding
import com.example.bachelorwork.databinding.ProductItemInOrderSelectionBinding
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.OrderSelectableProductUi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderAddProductModalBottomSheetFragment: BaseBottomSheetDialogFragment<FragmentModalBottomSheetOrderAddProductBinding>()
{

    override val bindingInflater : (LayoutInflater,ViewGroup?,Boolean) -> FragmentModalBottomSheetOrderAddProductBinding
        get() = FragmentModalBottomSheetOrderAddProductBinding::inflate

    override val isAllowFullScreen : Boolean = false

    override val isFullScreen : Boolean = false

    private val viewModel : OrderAddProductViewModel by viewModels()

    private val adapter : SimpleBindingAdapter<OrderSelectableProductUi> =
        simpleAdapter<OrderSelectableProductUi,ProductItemInOrderSelectionBinding> {
            bind { product ->
                this.textViewName.text = product.name
            }
            listeners {
                this.root.apply {
                    setOnClickListener {
                        alpha = 0.5f
                        setBackgroundColor(Color.RED)
                        showAllOtherComponents()
                    }
                }

                this.checkboxDetail.onClick { product ->
                    viewModel.navigateToProductDetail(product.id)
                }
            }
        }

    override fun setupViews()
    {
        setupSearchBar()
        setupRecycleView()

        collectInLifecycle(viewModel.uiState) {
            setupUiState(it)
        }

    }

    private fun setupUiState(uiState : OrderAddProductUiState)
    {
        if (uiState.searchQuery.isNotEmpty())
        {
            adapter.submitList(uiState.products) {
                binding.recyclerViewProducts.visibility = View.VISIBLE
                binding.textViewEnterOrScan.visibility = View.INVISIBLE
            }
        } else
        {
            binding.recyclerViewProducts.visibility = View.INVISIBLE
            binding.textViewEnterOrScan.visibility = View.INVISIBLE
            hideAllComponents()
        }
    }

    private fun setupRecycleView()
    {
        with(binding.recyclerViewProducts) {
            setHasFixedSize(true)
            adapter = this@OrderAddProductModalBottomSheetFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupSearchBar() {
        binding.editTextSearch.doAfterTextChanged {
            viewModel.searchProductByName(it.toString())
        }
        binding.textInputLayoutSearch.setEndIconOnClickListener {
            viewModel.scanBarcodeScanner()
        }
    }

    private fun showAllOtherComponents() {
        binding.textInputLayoutQuantity.visibility = View.VISIBLE
        binding.textInputLayoutRate.visibility = View.VISIBLE
        binding.buttonAddProduct.visibility = View.VISIBLE
    }

    private fun hideAllComponents() {
        binding.textInputLayoutQuantity.visibility = View.GONE
        binding.textInputLayoutRate.visibility = View.GONE
        binding.buttonAddProduct.visibility = View.GONE
    }
}