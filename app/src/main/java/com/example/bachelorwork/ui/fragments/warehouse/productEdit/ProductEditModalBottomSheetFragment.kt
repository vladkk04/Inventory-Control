package com.example.bachelorwork.ui.fragments.warehouse.productEdit

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetProductManageBinding
import com.example.bachelorwork.domain.model.product.ProductUnit
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.productManage.ProductEditUIState
import com.example.bachelorwork.ui.model.productManage.ProductManageFormEvent
import com.example.bachelorwork.ui.model.productManage.ProductManageFormState
import com.example.bachelorwork.ui.utils.dialogs.createDiscardDialog
import com.example.bachelorwork.ui.utils.inputFilters.NoZeroInputFilter
import com.example.bachelorwork.util.namesTyped
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProductEditModalBottomSheetFragment(
) : BaseBottomSheetDialogFragment<FragmentModalBottomSheetProductManageBinding>() {

    private val viewModel: ProductEditViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetProductManageBinding
        get() = FragmentModalBottomSheetProductManageBinding::inflate

    override val inflateMenu: Int
        get() = R.menu.bottom_sheet_modify_product_menu

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.save -> {
                viewModel.updateProduct()
                true
            }

            else -> false
        }
    }

    override fun onNavigationIconToolbarClickListener() {
        createDiscardDialog(onPositiveButtonClick = { dismiss() }).show()
    }

    override fun setupViews() {
        setupUIComponents()

        viewLifecycleOwner.collectInLifecycle(viewModel.uiState) { uiState ->
            updateUIState(uiState)
        }
        viewLifecycleOwner.collectInLifecycle(viewModel.uiFormState) { uiState ->
            updateFormFieldUIState(uiState)
        }
    }

    private fun setupUIComponents() {
        //binding.shapeableImageViewProduct.setOnClickListener { viewModel.select() }
        setupBarcodeScanner()
        setupNumberPicker()
        setupInputEditTextChangeListeners()
        setupInputFieldQuantity()
    }

    private fun setupNumberPicker() {
        binding.numberPickerSelectUnit.apply {
            maxValue = ProductUnit.entries.size - 1
            displayedValues = ProductUnit.entries.namesTyped()
        }.setOnScrollListener { view, _ ->
            viewModel.onEvent(ProductManageFormEvent.UnitChanged(ProductUnit.entries[view.value]))
        }
    }

    private fun setupBarcodeScanner() {
        binding.textInputLayoutBarcode.apply {
            setEndIconOnClickListener { viewModel.startScanBarcode() }
            setErrorIconOnClickListener { viewModel.startScanBarcode() }
        }
    }

    private fun setupInputFieldQuantity() {
        binding.textInputLayoutQuantity.apply {
            /*Plus Icon*/
            setEndIconOnClickListener { viewModel.increaseQuantity() }
            /*Minus Icon*/
            setStartIconOnClickListener { viewModel.decreaseQuantity() }
        }

        binding.editTextQuantity.apply {
            setOnFocusChangeListener { _, hasFocus ->
                setText(if (hasFocus) text else viewModel.uiFormState.value.quantity.toString())
            }

            filters = arrayOf(NoZeroInputFilter)
        }
    }

    private fun setupInputEditTextChangeListeners() {
        with(binding) {
            setupTextChangeListener(editTextName, ProductManageFormEvent::NameChanged)
            setupTextChangeListener(editTextBarcode, ProductManageFormEvent::BarcodeChanged)
            setupTextChangeListener(editTextQuantity, ProductManageFormEvent::QuantityChanged)
            setupTextChangeListener(editTextMinStockLevel, ProductManageFormEvent::MinStockLevelChanged)
            setupTextChangeListener(editTextDescription, ProductManageFormEvent::DescriptionChanged)

            binding.customInputLayoutTags.onTextChangeListener { tags ->
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

    private fun updateUIState(uiState: ProductEditUIState) {

        if(uiState.product != null) {
            binding.editTextName.setText(uiState.product.name)
            binding.editTextBarcode.setText(uiState.product.barcode)
            binding.editTextMinStockLevel.setText(uiState.product.minStockLevel.toString())
            binding.editTextQuantity.setText(String.format(Locale.getDefault(), "%d", uiState.product.quantity))
            viewModel.onEvent(ProductManageFormEvent.CategoryChanged(uiState.product.category))

            binding.editTextDescription.setText(uiState.product.description)
            binding.customInputLayoutTags.addTags(*uiState.product.tags.toTypedArray())
            binding.editTextBarcode.setText(uiState.product.barcode)
        }


    }

    private fun updateFormFieldUIState(uiStateForm: ProductManageFormState) {
        updateFormFieldErrors(uiStateForm)
        updateUIElementsFromState(uiStateForm)
    }

    private fun updateUIElementsFromState(uiStateForm: ProductManageFormState) {
        binding.editTextQuantity.apply {
            setText(String.format(Locale.getDefault(), "%d", uiStateForm.quantity))
            setSelection(uiStateForm.quantity.toString().length)
        }
        //binding.autoCompleteTextViewCategory.setText(uiStateForm.category.name, false)
    }

    private fun updateFormFieldErrors(uiStateForm: ProductManageFormState) {
        binding.textInputLayoutName.error = uiStateForm.nameError
        binding.textInputLayoutBarcode.error = uiStateForm.barcodeError
        binding.textInputLayoutMinStockLevel.error = uiStateForm.minStockLevelError
        //binding.textInputLayoutCategory.error = uiStateForm.categoryError
    }

}


