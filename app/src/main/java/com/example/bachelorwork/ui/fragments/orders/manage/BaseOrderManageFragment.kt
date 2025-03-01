package com.example.bachelorwork.ui.fragments.orders.manage

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrderManageBinding
import com.example.bachelorwork.databinding.OrderCreateAttachmentItemBinding
import com.example.bachelorwork.databinding.OrderCreateProductItemBinding
import com.example.bachelorwork.domain.model.FileData
import com.example.bachelorwork.domain.model.FileMimeType
import com.example.bachelorwork.domain.model.order.OrderAddedProduct
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.order.DiscountType
import com.example.bachelorwork.ui.navigation.Destination
import com.example.bachelorwork.ui.snackbar.SnackbarController
import com.example.bachelorwork.ui.utils.FilePicker
import com.example.bachelorwork.ui.utils.extensions.hiltViewModelNavigation
import java.util.Locale

abstract class BaseOrderManageFragment: BaseBottomSheetDialogFragment<FragmentOrderManageBinding>() {

    abstract val viewModel: BaseOrderManageViewModel

    private val sharedViewModel: OrderManageProductSharedViewModel by hiltViewModelNavigation(
        Destination.CreateOrder
    )

    private val adapter = simpleAdapter<OrderAddedProduct, OrderCreateProductItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind {
            this.textViewName.text = it.name
            this.textViewAmount.text = getString(
                R.string.text_amount_order_added_product,
                it.quantity,
                it.unit.name,
                it.rate
            )
            this.textViewTotal.text = String.format(Locale.getDefault(), "%.2f", it.total)

            this.checkBoxRemove.isChecked = false
            this.checkBoxEdit.isChecked = false
        }
        listeners {
            this.checkBoxRemove.onClick { item ->
                //viewModel.deleteAddedProduct(item)
            }
            this.checkBoxEdit.onClick { item ->
                //viewModel.navigateToOrderEditAddedProduct(item)
            }
        }
    }

    override val inflateMenu: Int
        get() = R.menu.toolbar_manage_order_menu

    override fun onNavigationIconToolbarClickListener() {
        AppDialogs.createDiscardDialog(requireContext()) { dismiss() }.show()
    }

    override fun setupViews() {
        setupAddProductToOrderLayout()
        setupDiscountSelector()

        SnackbarController.observeSnackbarEvents(this, binding.root)
    }

    private val fileLauncher =
        registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { result ->
            FilePicker().apply {

            }
        }

    private val fileAdapter = simpleAdapter<FileData, OrderCreateAttachmentItemBinding> {
        bind { item ->
            val attachmentIcon = ContextCompat.getDrawable(
                requireContext(),
                when (item.mimeType) {
                    FileMimeType.PDF -> R.drawable.ic_file_pdf
                    null -> R.drawable.ic_file_pdf
                    else -> R.drawable.ic_file_image
                }
            )

            this.imageViewAttachment.setImageDrawable(attachmentIcon)
            this.textViewAttachmentName.text = item.displayName
            this.textViewAttachmentSize.text = item.size
        }
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@BaseOrderManageFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupRecyclerViewFiles() {
        with(binding.recyclerViewFiles) {
            adapter = this@BaseOrderManageFragment.fileAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupDiscountSelector() {
        binding.radioButtonPercentage.setOnClickListener {
            viewModel.navigateToOrderManageDiscount(DiscountType.PERCENTAGE)
        }

        binding.radioButtonFixed.setOnClickListener {
            viewModel.navigateToOrderManageDiscount(DiscountType.FIXED)
        }
    }


    private fun setupAddProductToOrderLayout() {
        binding.layoutAddProduct.setOnClickListener {
            viewModel.navigateToOrderAddProduct()
        }
        binding.buttonAddProduct.setOnClickListener {
            viewModel.navigateToOrderAddProduct()
        }
    }

}