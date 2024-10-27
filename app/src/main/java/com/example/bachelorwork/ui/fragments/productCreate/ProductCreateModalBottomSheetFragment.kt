package com.example.bachelorwork.ui.fragments.productCreate

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.FragmentModalBottomSheetProductCreateBinding
import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.model.ProductUnit
import com.example.bachelorwork.ui.common.adapters.CategoryArrayAdapter
import com.example.bachelorwork.ui.common.base.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.common.base.showDatePicker
import com.example.bachelorwork.ui.constant.Constants
import com.example.bachelorwork.ui.model.productCreate.ProductCreateFormEvent
import com.example.bachelorwork.ui.model.productCreate.ProductCreateFormState
import com.example.bachelorwork.ui.model.productCreate.ProductCreateUIState
import com.example.bachelorwork.ui.utils.dialogs.CategoryDialogType
import com.example.bachelorwork.ui.utils.dialogs.createCategoryDialog
import com.example.bachelorwork.ui.utils.dialogs.createDeleteDialog
import com.example.bachelorwork.ui.utils.dialogs.createDiscardDialog
import com.example.bachelorwork.ui.utils.inputFilters.NoZeroInputFilter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ProductCreateModalBottomSheetFragment :
    BaseBottomSheetDialogFragment<FragmentModalBottomSheetProductCreateBinding>() {

    private val viewModel: ProductCreateViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentModalBottomSheetProductCreateBinding
        get() = FragmentModalBottomSheetProductCreateBinding::inflate

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.shapeableImageViewProduct)
    }

    override fun setupCustomToolbar(): MaterialToolbar = binding.toolbarCreateProduct

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.item_save_new_product -> {
                viewModel.onEvent(ProductCreateFormEvent.CreateProduct)
                true
            }

            else -> false
        }
    }

    override fun onNavigationIconToolbarClickListener() {
        createDiscardDialog(onPositiveButtonClick = { dismiss() })
    }

    override fun setupViews() {
        setupInputEditTextFields()
        setupOnInputFieldIconClickListeners()

        setupNumberPicker()
        setupImageViewOnClickListener()

        viewLifecycleOwner.lifecycleScope.launch {
            launch { observeUIState() }
            launch { observeFormUIState() }
        }
    }

    private fun setupNumberPicker() {
        binding.numberPickerCreateProductSelectUnit.apply {
            minValue = 0
            maxValue = ProductUnit.entries.size - 1
            displayedValues = ProductUnit.entries.map { it.name }.toTypedArray()
        }.setOnScrollListener { view, _ ->
            viewModel.onEvent(ProductCreateFormEvent.UnitChanged(ProductUnit.entries[view.value]))
        }
    }

    private fun setupImageViewOnClickListener() {
        binding.shapeableImageViewProduct.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    private fun setupOnInputFieldIconClickListeners() {
        binding.textInputLayoutQuantity.apply {
            /*Minus Icon*/
            setEndIconOnClickListener { viewModel.increaseQuantity() }
            /*Plus Icon*/
            setStartIconOnClickListener { viewModel.decreaseQuantity() }
        }

        binding.textInputLayoutBarcode.setEndIconOnClickListener { viewModel.startScanBarcode() }

        binding.textInputLayoutDatePurchase.helperText =
            Constants.DEFAULT_DATE_FORMAT_PATTERN.uppercase()
    }

    private fun setupInputEditTextFields() {
        binding.editTextName.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.NameChanged(it.toString()))
        }
        binding.editTextBarcode.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.BarcodeChanged(it.toString()))
        }
        binding.editTextQuantity.apply {
            doAfterTextChanged {
                viewModel.onEvent(ProductCreateFormEvent.QuantityChanged(it.toString()))
            }
            setOnFocusChangeListener { _, hasFocus ->
                setText(if (hasFocus) text else viewModel.uiFormState.value.quantity.toString())
            }
            filters = arrayOf(NoZeroInputFilter)
        }
        binding.editTextPricePerUnit.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.PricePerUnitChanged(it.toString()))
        }
        binding.editTextDatePurchase.apply {
            doAfterTextChanged {
                viewModel.onEvent(ProductCreateFormEvent.DatePurchaseChanged(it.toString()))
            }
            setOnClickListener {
                showDatePicker { date -> setText(date) }
            }
        }
        binding.editTextMinStockLevel.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.MinStockLevelChanged(it.toString()))
        }
        binding.autoCompleteTextViewCategory.doAfterTextChanged {
            //viewModel.onEvent(ProductCreateFormEvent.CategoryChanged(it.toString()))
        }
        /*binding.editTextTags.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.TagsChanged(emptyList()))
        }*/
        binding.editTextDescription.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.DescriptionChanged(it.toString()))
        }
        setupEditTextFieldTags()
    }

    private fun setupAutoCompleteTextViewCategory(categories: List<ProductCategory>) {
        (binding.autoCompleteTextViewCategory as? MaterialAutoCompleteTextView)?.setAdapter(
            setupCategoryAdapter(categories)
        )
    }

    private fun setupCategoryAdapter(categories: List<ProductCategory>): CategoryArrayAdapter =
        CategoryArrayAdapter(
            requireContext(),
            categories.toMutableList()
        ).apply {
            setOnCreateNewCategory {
                createCategoryDialog(type = CategoryDialogType.CREATE) {
                    viewModel.createCategory(name = it.name)
                }.show()
            }
            setOnEditClickListener { item ->
                createCategoryDialog(item, CategoryDialogType.EDIT) {
                    //viewModel.createCategory(name = it.name)
                }.show()
            }
            setOnDeleteClickListener { item ->
                createDeleteDialog("category") {
                    viewModel.deleteCategory(item)
                }.show()
            }
        }

    private fun setupEditTextFieldTags() {
        binding.customInputLayoutTags.addNewTag("hello")
        binding.customInputLayoutTags.addNewTag("nigga")
    }

    private suspend fun observeUIState() = viewModel.uiState.collectLatest { uiState ->
        updateUIState(uiState)
    }

    private suspend fun observeFormUIState() = viewModel.uiFormState.collectLatest { uiState ->
        updateFormUIState(uiState)
    }

    private fun updateUIState(uiState: ProductCreateUIState) {
        if (uiState.isProductCreated) {
            dismiss()
        }
        setupAutoCompleteTextViewCategory(uiState.categories)
    }

    private fun updateFormUIState(uiStateForm: ProductCreateFormState) {
        binding.textInputLayoutName.error = uiStateForm.nameError
        binding.textInputLayoutBarcode.error = uiStateForm.barcodeError
        binding.textInputLayoutPricePerUnit.apply {
            error = uiStateForm.pricePerUnitError
            hint = requireContext().getString(R.string.text_price_per_unit, uiStateForm.unit)
        }
        binding.textInputLayoutCategory.error = uiStateForm.categoryError
        binding.textInputLayoutDatePurchase.error = uiStateForm.datePurchaseError
        binding.textInputLayoutMinStockLevel.error = uiStateForm.minStockLevelError

        binding.editTextQuantity.apply {
            setText(String.format(Locale.getDefault(), "%d", uiStateForm.quantity))
            setSelection(uiStateForm.quantity.toString().length)
        }

        binding.textViewTotalPrice.text =
            requireContext().getString(R.string.text_total_price, uiStateForm.totalPrice)
    }
}

