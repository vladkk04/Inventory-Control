package com.example.inventorycotrol.ui.fragments.orders.manage.discount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentOrderDiscountManageBinding
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.model.order.DiscountType
import com.example.inventorycotrol.ui.model.order.discount.OrderManageDiscountUiState
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderManageDiscountFragment : BaseBottomSheetDialogFragment<FragmentOrderDiscountManageBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderDiscountManageBinding
        get() = FragmentOrderDiscountManageBinding::inflate

    private val viewModel: OrderManageDiscountViewModal by viewModels()

    override val isFullScreen: Boolean = false

    override val isAllowFullScreen: Boolean = false


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
        binding.textInputDiscount.endIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            discountIcon
        )
        binding.textInputDiscount.errorIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            discountIcon
        )
        binding.textInputDiscount.error = uiState.discountError
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