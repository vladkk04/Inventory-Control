package com.example.bachelorwork.ui.fragments.warehouse.productManage

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentProductManageBinding
import com.example.bachelorwork.domain.model.product.ProductUnit
import com.example.bachelorwork.ui.common.AppDialogs
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.product.manage.ProductManageFormEvent
import com.example.bachelorwork.ui.model.product.manage.ProductManageFormState
import com.example.bachelorwork.ui.utils.activityResultContracts.VisualMediaPicker
import com.example.bachelorwork.ui.utils.extensions.collectInLifecycle
import com.example.bachelorwork.util.namesTyped
import com.yalantis.ucrop.UCrop

abstract class BaseProductManageFragment :
    BaseBottomSheetDialogFragment<FragmentProductManageBinding>() {

    abstract val viewModel: BaseProductManageViewModel

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentProductManageBinding
        get() = FragmentProductManageBinding::inflate

    override val inflateMenu: Int
        get() = R.menu.toolbar_manage_product_menu

    override fun onNavigationIconToolbarClickListener() {
        AppDialogs.createDiscardDialog(requireContext(), onPositiveButtonClick = { dismiss() })
            .show()
    }

    private val visualMediaPicker by lazy { VisualMediaPicker(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        visualMediaPicker.setupUCropOptions(UCrop.Options().apply {
            withAspectRatio(2f, 1f)
            withMaxResultSize(768, 768)
            setCompressionFormat(Bitmap.CompressFormat.JPEG)
            setCompressionQuality(80)
        })
    }

    override fun setupViews() {
        setupUIComponents()


        viewLifecycleOwner.collectInLifecycle(viewModel.uiState) { uiState ->
            if (uiState.isManaged) {
                dismiss()
            }
        }

        viewLifecycleOwner.collectInLifecycle(viewModel.uiFormState) { uiState ->
            updateFormFieldUIState(uiState)
        }

        collectInLifecycle(viewModel.image) {

        }

        collectInLifecycle(viewModel.barcode) {
            binding.editTextBarcode.setText(it)
        }

        visualMediaPicker.addCallbackResult { uri ->

            viewModel.setupImage(uri)

            uri?.let {
                Glide.with(this)
                    .load(it)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(binding.imageView)
            }
        }
    }

    private fun setupUIComponents() {
        binding.imageView.setOnClickListener {
            visualMediaPicker.launchVisualMediaPicker()
        }
        setupBarcodeScanner()
        setupNumberPicker()
        setupInputEditTextChangeListeners()
    }

    private fun setupNumberPicker() {
        binding.numberPickerUnit.apply {
            maxValue = ProductUnit.entries.size - 1
            displayedValues = ProductUnit.entries.namesTyped()
        }.setOnValueChangedListener { _, _, current ->
            viewModel.onEvent(ProductManageFormEvent.UnitChanged(ProductUnit.entries[current]))
        }
    }

    private fun setupBarcodeScanner() {
        binding.textInputBarcode.apply {
            setEndIconOnClickListener { viewModel.startScanBarcode() }
            setErrorIconOnClickListener { viewModel.startScanBarcode() }
        }
    }

    private fun setupInputEditTextChangeListeners() {
        with(binding) {
            setupTextChangeListener(editTextName, ProductManageFormEvent::NameChanged)
            setupTextChangeListener(editTextBarcode, ProductManageFormEvent::BarcodeChanged)
            setupTextChangeListener(editTextQuantity, ProductManageFormEvent::QuantityChanged)
            setupTextChangeListener(editTextMinStockLevel, ProductManageFormEvent::MinStockLevelChanged)
            setupTextChangeListener(editTextDescription, ProductManageFormEvent::DescriptionChanged)

            binding.customInputCategories.setOnClickItemListener {
                viewModel.onEvent(ProductManageFormEvent.CategoryChanged(it.id))
            }

            binding.customInputTags.setOnTextChangeListener { tags ->
                viewModel.onEvent(ProductManageFormEvent.TagsChanged(tags))
            }
        }
    }

    private fun setupTextChangeListener(
        editText: EditText,
        event: (String) -> ProductManageFormEvent,
    ) {
        editText.doAfterTextChanged {
            viewModel.onEvent(event.invoke(it.toString()))
        }
    }


    private fun updateFormFieldUIState(uiStateForm: ProductManageFormState) {
        updateFormFieldErrors(uiStateForm)
    }

    private fun updateFormFieldErrors(uiStateForm: ProductManageFormState) {
        binding.textInputName.error = uiStateForm.nameError
        binding.textInputBarcode.error = uiStateForm.barcodeError
        binding.textInputQuantity.error = uiStateForm.quantityError
        binding.textInputMinStockLevel.error = uiStateForm.minStockLevelError
        binding.customInputCategories.setErrorMessage(uiStateForm.categoryError)
    }
}