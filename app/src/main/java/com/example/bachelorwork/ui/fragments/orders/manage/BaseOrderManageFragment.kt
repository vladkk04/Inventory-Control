package com.example.bachelorwork.ui.fragments.orders.manage

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elveum.elementadapter.simpleAdapter
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentOrderManageBinding
import com.example.bachelorwork.databinding.OrderAttachmentItemBinding
import com.example.bachelorwork.databinding.OrderCreateProductItemBinding
import com.example.bachelorwork.domain.model.order.OrderAddedProduct
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.order.DiscountType
import com.example.bachelorwork.ui.model.order.manage.OrderManageUiState
import com.example.bachelorwork.ui.utils.FileData
import com.example.bachelorwork.ui.utils.FilePicker
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import java.util.Locale

abstract class BaseOrderManageFragment :
    BaseBottomSheetDialogFragment<FragmentOrderManageBinding>() {

    abstract val viewModel: BaseOrderManageViewModel

    abstract val sharedViewModel: OrderManageProductSharedViewModel

    private val adapter = simpleAdapter<OrderAddedProduct, OrderCreateProductItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind {
            this.textViewName.text = it.name
            this.textViewAmount.text = getString(
                R.string.text_amount_order_added_product,
                it.quantity,
                it.unit.name,
                it.price
            )
            this.textViewTotal.text = String.format(Locale.getDefault(), "%.2f", it.total)
            Glide.with(requireContext())
                .load("http://192.168.68.60:8080/${it.image}")
                .placeholder(R.drawable.ic_image)
                .fallback(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .into(imageView)

            this.checkBoxRemove.isChecked = false
        }
        listeners {
            this.checkBoxRemove.onClick { item ->
                viewModel.deleteAddedProduct(item)
            }
        }
    }

    private val attachmentsAdapter = simpleAdapter<FileData, OrderAttachmentItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.displayName == newItem.displayName }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind { item ->
            this.textViewAttachmentName.text = item.displayName
            this.textViewAttachmentSize.text = item.size
            this.checkBoxDelete.isVisible = true
        }
        listeners {
            this.checkBoxDelete.onClick { item ->
                viewModel.removeAttachment(item)
            }
        }
    }
    
    private val filePicker = FilePicker()

    override val inflateMenu: Int
        get() = R.menu.toolbar_manage_order_menu

    override fun onNavigationIconToolbarClickListener() {
        AppDialogs.createDiscardDialog(requireContext()) { dismiss() }.show()
    }

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { result ->
        val fileData = result.map { filePicker.dumpImageMetaData(it, requireContext().contentResolver) }

        viewModel.addAttachments(fileData)
    }

    override fun setupViews() {
        setupAddProductToOrderLayout()
        setupDiscountSelector()
        setupRecyclerView()
        setupAttachmentRecyclerView()
        setupAddAttachmentsToOrderLayout()

        collectInLifecycle(sharedViewModel.selectedProduct) {
            if (it != null) {
                viewModel.addProductToOrder(it)
            }
        }

        collectInLifecycle(viewModel.uiState) {
            updateUiState(it)
        }

        collectInLifecycle(
            findNavController().currentBackStackEntry?.savedStateHandle
                ?.getStateFlow("discount", 0.00)
                ?: return
        ) {
            viewModel.setDiscount(it)
        }

        binding.editTextComment.doAfterTextChanged {
            viewModel.changeComment(it.toString())
        }
    }


    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@BaseOrderManageFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupAttachmentRecyclerView() {
        with(binding.recyclerViewFiles) {
            adapter = this@BaseOrderManageFragment.attachmentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun updateUiState(uiState: OrderManageUiState) {
        binding.radioButtonFixed.isEnabled = uiState.subtotal > 0
        binding.layoutAddProduct.isVisible = uiState.addedProduct.isEmpty()
        binding.textViewTotal.text = getString(R.string.text_total_value, uiState.total)
        binding.textViewSubtotal.text = getString(R.string.text_subtotal_value, uiState.subtotal)
        binding.textViewDiscount.text = getString(R.string.text_discount_value, uiState.discount)
        adapter.submitList(uiState.addedProduct.toList())
        attachmentsAdapter.submitList(uiState.attachments)
    }

    private fun setupDiscountSelector() {
        binding.radioButtonPercentage.setOnClickListener {
            viewModel.navigateToOrderManageDiscount(DiscountType.PERCENTAGE)
        }

        binding.radioButtonFixed.setOnClickListener {
            viewModel.navigateToOrderManageDiscount(DiscountType.FIXED)
        }
    }

    private fun setupAddAttachmentsToOrderLayout() {
        binding.textViewUploadFiles.setOnClickListener {
            filePickerLauncher.launch("*/*")
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