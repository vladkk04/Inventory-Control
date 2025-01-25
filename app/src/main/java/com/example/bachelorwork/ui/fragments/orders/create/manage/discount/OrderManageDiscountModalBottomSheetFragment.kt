package com.example.bachelorwork.ui.fragments.orders.create.manage.discount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetOrderManageDiscountBinding
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.order.create.DiscountType
import com.example.bachelorwork.ui.model.order.create.manage.discount.OrderManageDiscountUiState
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderManageDiscountModalBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentModalBottomSheetOrderManageDiscountBinding>() {

    private val viewModel: OrderManageDiscountViewModal by viewModels()

    override val isFullScreen: Boolean
        get() = false

    override val isAllowFullScreen: Boolean
        get() = false

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetOrderManageDiscountBinding
        get() = FragmentModalBottomSheetOrderManageDiscountBinding::inflate

    override fun setupViews() {
        setupApplyButton()
        setupDiscountEditTextChanges()

        collectInLifecycle(viewModel.uiState, Lifecycle.State.STARTED) {
            updateUI(it)
        }
    }

    private fun updateUI(uiState: OrderManageDiscountUiState) {
        val discountIcon = when (uiState.discountType) {
            DiscountType.PERCENTAGE -> R.drawable.ic_percent
            DiscountType.FIXED -> R.drawable.ic_lock
        }
        binding.textInputLayoutDiscount.endIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            discountIcon
        )
        binding.textInputLayoutDiscount.error = uiState.discountError
    }

    private fun setupDiscountEditTextChanges() {
        binding.editTextDiscount.setText(viewModel.currentDiscount)

        binding.editTextDiscount.doAfterTextChanged {
            viewModel.updateDiscount(it.toString())
        }
    }

    private fun setupApplyButton() {
        binding.buttonApply.setOnClickListener {
            viewModel.saveDiscountAndNavigateUp()
        }
    }
}