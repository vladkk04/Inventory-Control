package com.example.inventorycotrol.ui.fragments.orders.manage.create

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.elveum.elementadapter.simpleAdapter
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.databinding.FragmentOrderManageBinding
import com.example.inventorycotrol.databinding.OrderAttachmentItemBinding
import com.example.inventorycotrol.databinding.OrderCreateProductItemBinding
import com.example.inventorycotrol.domain.model.order.OrderAddedProduct
import com.example.inventorycotrol.domain.model.file.FileData
import com.example.inventorycotrol.domain.model.file.FileMimeType
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.fragments.orders.manage.OrderManageProductSharedViewModel
import com.example.inventorycotrol.ui.model.order.DiscountType
import com.example.inventorycotrol.ui.model.order.manage.OrderManageUiState
import com.example.inventorycotrol.ui.navigation.Destination
import com.example.inventorycotrol.ui.utils.FilePicker
import com.example.inventorycotrol.ui.utils.extensions.hiltViewModelNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class OrderCreateFragment :  BaseBottomSheetDialogFragment<FragmentOrderManageBinding>() {

    private val viewModel: OrderCreateViewModel by viewModels()

    private val sharedViewModel: OrderManageProductSharedViewModel by hiltViewModelNavigation(Destination.CreateOrder)

    private val adapter = simpleAdapter<OrderAddedProduct, OrderCreateProductItemBinding> {
        areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
        areContentsSame = { oldItem, newItem -> oldItem == newItem }

        bind {
            this.textViewName.text = it.name
            this.textViewAmount.text = getString(
                R.string.text_amount_order_added_product,
                it.quantity,
                it.unit.name,
                it.price,
                viewModel.uiState.value.currency
            )
            this.textViewTotal.text = String.format(Locale.getDefault(), "%.2f %s", it.total, viewModel.uiState.value.currency)

            it.image?.let { image ->
                Glide.with(requireContext())
                    .load("${AppConstants.BASE_URL_CLOUD_FRONT}${image}")
                    .placeholder(R.drawable.ic_image)
                    .fallback(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
                    .into(imageView)
            }

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

            val icon = when (item.mimeType) {
                FileMimeType.PDF -> {
                    R.drawable.ic_file_pdf
                }

                FileMimeType.PNG -> {
                    R.drawable.ic_file_image
                }

                FileMimeType.JPG -> {
                    R.drawable.ic_file_image
                }

                FileMimeType.JPEG -> {
                    R.drawable.ic_file_image
                }

                FileMimeType.DOC -> {
                    R.drawable.ic_doc_file
                }

                FileMimeType.DOCX -> {
                    R.drawable.ic_doc_file
                }

                FileMimeType.TXT -> {
                    R.drawable.ic_txt_file
                }

                FileMimeType.XLS -> {
                    R.drawable.ic_excel_file
                }

                FileMimeType.XLSX -> {
                    R.drawable.ic_excel_file
                }
                null -> {
                    R.drawable.ic_outline_file
                }
            }

            this.imageViewAttachment.setImageResource(icon)
        }
        listeners {
            this.checkBoxDelete.onClick { item ->
                viewModel.removeAttachment(item)
            }
        }
    }

    private val filePicker = FilePicker()

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.OpenMultipleDocuments()
    ) { result ->
        val fileData = result.map { filePicker.dumpImageMetaData(it, requireContext().contentResolver) }

        viewModel.addAttachments(fileData)
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentOrderManageBinding
        get() = FragmentOrderManageBinding::inflate

    override val titleToolbar: String = "Create Order"

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                viewModel.createOrder()
                true
            }

            else -> false
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
        setupRecyclerView()
        setupAttachmentRecyclerView()
        setupAddAttachmentsToOrderLayout()

        viewLifecycleOwner.lifecycleScope.launch {
            sharedViewModel.selectedProduct.collectLatest {
                it?.let { viewModel.addProductToOrder(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect {
                showProgress(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect {
                updateUiState(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            findNavController().currentBackStackEntry?.savedStateHandle?.getStateFlow("discount", 0.00)?.collectLatest {
                viewModel.setDiscount(it)
            }
        }
        binding.editTextComment.doAfterTextChanged {
            viewModel.changeComment(it.toString())
        }
    }


    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            adapter = this@OrderCreateFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupAttachmentRecyclerView() {
        with(binding.recyclerViewFiles) {
            adapter = this@OrderCreateFragment.attachmentsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun updateUiState(uiState: OrderManageUiState) {
        binding.radioButtonFixed.isEnabled = uiState.subtotal > 0
        binding.layoutAddProduct.isVisible = uiState.addedProduct.isEmpty()
        binding.textViewTotal.text = getString(R.string.text_total_value, uiState.total, uiState.currency)
        binding.textViewSubtotal.text = getString(R.string.text_subtotal_value, uiState.subtotal, uiState.currency)
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
            filePickerLauncher.launch(AppConstants.SUPPORTED_MIME_TYPES_FOR_PICKER)
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