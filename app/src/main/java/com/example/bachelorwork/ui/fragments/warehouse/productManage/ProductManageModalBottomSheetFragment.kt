package com.example.bachelorwork.ui.fragments.warehouse.productManage

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetProductManageBinding
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.domain.model.product.ProductUnit
import com.example.bachelorwork.ui.collectInLifecycle
import com.example.bachelorwork.ui.common.adapters.CategoryArrayAdapter
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.model.productManage.ProductCreateFormEvent
import com.example.bachelorwork.ui.model.productManage.ProductCreateFormState
import com.example.bachelorwork.ui.model.productManage.ProductManageUIState
import com.example.bachelorwork.ui.utils.dialogs.CategoryDialogType
import com.example.bachelorwork.ui.utils.dialogs.createCategoryDialog
import com.example.bachelorwork.ui.utils.dialogs.createDeleteDialog
import com.example.bachelorwork.ui.utils.dialogs.createDiscardDialog
import com.example.bachelorwork.ui.utils.inputFilters.NoZeroInputFilter
import com.example.bachelorwork.util.namesTyped
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ProductManageModalBottomSheetFragment(
) : BaseBottomSheetDialogFragment<FragmentModalBottomSheetProductManageBinding>() {

    private val viewModel: ProductManageViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetProductManageBinding
        get() = FragmentModalBottomSheetProductManageBinding::inflate

    override fun setupCustomToolbar(): MaterialToolbar = binding.toolbarManageProduct

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.product_manage -> {
                viewModel.modifyProduct()
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
            viewModel.onEvent(ProductCreateFormEvent.UnitChanged(ProductUnit.entries[view.value]))
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
            setupTextChangeListener(editTextName, ProductCreateFormEvent::NameChanged)
            setupTextChangeListener(editTextBarcode, ProductCreateFormEvent::BarcodeChanged)
            setupTextChangeListener(editTextQuantity, ProductCreateFormEvent::QuantityChanged)
            setupTextChangeListener(editTextMinStockLevel, ProductCreateFormEvent::MinStockLevelChanged)
            setupTextChangeListener(editTextDescription, ProductCreateFormEvent::DescriptionChanged)

            binding.customInputLayoutTags.onTextChangeListener { tags ->
                viewModel.onEvent(ProductCreateFormEvent.TagsChanged(tags))
            }
        }
    }

    private fun setupTextChangeListener(
        editText: EditText,
        event: (String) -> ProductCreateFormEvent,
    ) {
        editText.doAfterTextChanged {
            viewModel.onEvent(event.invoke(it.toString()))
        }
    }

    private fun setupAutoCompleteTextViewCategory(categories: List<ProductCategory>) {
        (binding.autoCompleteTextViewCategory as? MaterialAutoCompleteTextView)?.setAdapter(
            setupCategoryAdapter(categories)
        )
    }

    private fun setupCategoryAdapter(categories: List<ProductCategory>): CategoryArrayAdapter {
        return CategoryArrayAdapter(
            requireContext(),
            categories.toMutableList()
        ).apply {
            setOnClickItemListener { item ->
                viewModel.onEvent(ProductCreateFormEvent.CategoryChanged(item))
                binding.autoCompleteTextViewCategory.dismissDropDown()
            }
            setOnCreateNewCategory {
                createCategoryDialog(type = CategoryDialogType.CREATE) {
                    viewModel.createCategory(it)
                }.show()
            }
            setOnEditClickListener { item ->
                createCategoryDialog(item, CategoryDialogType.EDIT) {
                    viewModel.updateCategory(it)
                }.show()
            }
            setOnDeleteClickListener { item ->
                createDeleteDialog(requireContext(), "category \"${item.name}\"") {
                    viewModel.deleteCategory(item)
                }.show()
            }
        }
    }

    private fun updateUIState(uiState: ProductManageUIState) {
        setupAutoCompleteTextViewCategory(uiState.categories)

        if(uiState.product != null) {
            binding.editTextName.setText(uiState.product.name)
            binding.editTextBarcode.setText(uiState.product.barcode)
            binding.editTextMinStockLevel.setText(uiState.product.minStockLevel.toString())
            binding.editTextQuantity.setText(String.format(Locale.getDefault(), "%d", uiState.product.quantity))
            viewModel.onEvent(ProductCreateFormEvent.CategoryChanged(uiState.product.category))

            binding.autoCompleteTextViewCategory.setText(uiState.product.category.name, false)
            binding.editTextDescription.setText(uiState.product.description)
            binding.customInputLayoutTags.addTags(*uiState.product.tags.toTypedArray())
            binding.editTextBarcode.setText(uiState.product.barcode)
        }

        binding.toolbarManageProduct.title = uiState.titleToolbar

    }

    private fun updateFormFieldUIState(uiStateForm: ProductCreateFormState) {
        updateFormFieldErrors(uiStateForm)
        updateUIElementsFromState(uiStateForm)
    }

    private fun updateUIElementsFromState(uiStateForm: ProductCreateFormState) {
        binding.editTextQuantity.apply {
            setText(String.format(Locale.getDefault(), "%d", uiStateForm.quantity))
            setSelection(uiStateForm.quantity.toString().length)
        }
        binding.autoCompleteTextViewCategory.setText(uiStateForm.category.name, false)
    }

    private fun updateFormFieldErrors(uiStateForm: ProductCreateFormState) {
        binding.textInputLayoutName.error = uiStateForm.nameError
        binding.textInputLayoutBarcode.error = uiStateForm.barcodeError
        binding.textInputLayoutMinStockLevel.error = uiStateForm.minStockLevelError
        binding.textInputLayoutCategory.error = uiStateForm.categoryError
    }

}


