package com.example.bachelorwork.ui.fragments.orders.manage.create

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrderManageBinding
import com.example.bachelorwork.ui.fragments.orders.manage.BaseOrderManageFragment
import com.example.bachelorwork.ui.snackbar.SnackbarEvent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrderCreateFragment : BaseOrderManageFragment() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderManageBinding
        get() = FragmentOrderManageBinding::inflate

    override val titleToolbar: String = "Create Order"

    override val viewModel: OrderCreateViewModel by viewModels()

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                viewModel.createOrder()
                true
            }

            else -> false
        }
    }

    override fun setupViews() {
        super.setupViews()


       /* setupRecyclerView()
        setupRecyclerViewFiles()

        setupAddProductToOrderLayout()
        setupDiscountSelector()*/

       /* collectInLifecycle(sharedViewModel.selectedProduct, Lifecycle.State.STARTED) {
            viewModel.addProductToOrder(it)
        }

        collectInLifecycle(viewModel.uiStateForm, Lifecycle.State.STARTED) {
            setupUiState(it)
        }
        collectInLifecycle(
            findNavController().currentBackStackEntry?.savedStateHandle
                ?.getStateFlow("discount", 0.00)
                ?: return
        ) {
            viewModel.setDiscount(it)
        }
        binding.materialCardViewFiles.setOnClickListener {
            fileLauncher.launch(arrayOf("application/pdf", "image/*"))
        }*/

        */
    }

    /*private fun setupUiState(uiStateForm: OrderCreateUiState) {
        if (uiStateForm.addedProduct.isEmpty()) {
            binding.layoutAddProduct.visibility = View.VISIBLE
        } else {
            binding.layoutAddProduct.visibility = View.GONE
        }

        val discountType = when (uiStateForm.discountType) {
            DiscountType.PERCENTAGE -> getString(
                R.string.text_discount_percentage,
                uiStateForm.discount
            )

            DiscountType.FIXED -> getString(R.string.text_discount_value, uiStateForm.discount)
        }


        binding.textViewSubtotal.text = getString(R.string.text_subtotal_value, uiStateForm.subtotal)
        binding.textViewDiscount.text = discountType
        binding.textViewTotal.text = getString(R.string.text_total_value, uiStateForm.total)

        //adapter.submitList(uiStateForm.addedProduct.toList())
    }*/




    /*
*/
}