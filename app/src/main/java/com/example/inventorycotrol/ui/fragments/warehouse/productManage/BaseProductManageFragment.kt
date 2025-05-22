package com.example.inventorycotrol.ui.fragments.warehouse.productManage

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.FragmentProductManageBinding
import com.example.inventorycotrol.domain.model.product.ProductUnit
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.common.base.BaseBottomSheetDialogFragment
import com.example.inventorycotrol.ui.model.product.manage.ProductManageFormEvent
import com.example.inventorycotrol.ui.model.product.manage.ProductManageFormState
import com.example.inventorycotrol.ui.utils.activityResultContracts.VisualMediaPicker
import com.example.inventorycotrol.ui.utils.extensions.collectInLifecycle
import com.example.inventorycotrol.ui.utils.extensions.requestImagePermissions
import com.example.inventorycotrol.util.namesTyped
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
            setMaxBitmapSize(2000)
            setCompressionFormat(Bitmap.CompressFormat.PNG)
            setCompressionQuality(80)
        })
    }

    override fun setupViews() {
        setupUIComponents()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { uiState ->
                showProgress(uiState.isLoading)
                if (uiState.isManaged) { dismiss() }
            }
        }

        viewLifecycleOwner.collectInLifecycle(viewModel.uiFormState) { uiState ->
            updateFormFieldUIState(uiState)
        }


        collectInLifecycle(viewModel.barcode) {
            binding.editTextBarcode.setText(it)
        }

        visualMediaPicker.addCallbackResult { uri ->
            viewModel.setupImage(uri)

            binding.layoutManageImage.isGone = true

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
            requestImagePermissions {
                if (viewModel.image.value != null) {
                    binding.layoutManageImage.isGone = false
                } else {
                    visualMediaPicker.launchVisualMediaPicker()
                }
            }
        }
        binding.textViewEdit.setOnClickListener {
            visualMediaPicker.launchVisualMediaPicker()
        }
        binding.textViewRemove.setOnClickListener {
            binding.imageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_add_image, null))
            binding.layoutManageImage.isGone = true
            viewModel.setupImage(null)
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

            customInputTags.setCustomHelperText(getString(R.string.helper_text_optional))
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