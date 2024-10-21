package com.example.bachelorwork.ui.productCreate

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bachelorwork.R
import com.google.android.material.R.*
import com.example.bachelorwork.databinding.ModalBottomSheetProductCreateBinding
import com.example.bachelorwork.domain.model.ProductCategory
import com.example.bachelorwork.domain.model.ProductUnit
import com.example.bachelorwork.ui.common.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.common.showDatePicker
import com.example.bachelorwork.ui.common.showDialog
import com.example.bachelorwork.ui.model.ProductCreateFormEvent
import com.example.bachelorwork.ui.model.ProductCreateFormState
import com.example.bachelorwork.ui.model.ProductCreateUIState
import com.example.bachelorwork.ui.productList.ProductListAdapter
import com.example.bachelorwork.ui.utils.inputFilters.NoLessThanZeroFilter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductCreateModalBottomSheet :
    BaseBottomSheetDialogFragment<ModalBottomSheetProductCreateBinding>() {

    private val viewModel: ProductCreateViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ModalBottomSheetProductCreateBinding
        get() = ModalBottomSheetProductCreateBinding::inflate

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.imageViewCreateProduct)
    }

    override fun setupCustomToolbar(): MaterialToolbar = binding.bottomSheetToolbar

    override fun onMenuItemToolbarClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.item_save_new_product -> {
                viewModel.onEvent(ProductCreateFormEvent.CreateProduct)
                true
            }

            else -> false
        }
    }

    override fun setupViews() {
        setupInputEditTextFields()
        setupOnInputFieldIconClickListeners()

        setupImageViewOnClickListener()
        setupNumberPicker()

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
        binding.imageViewCreateProduct.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    private fun setupOnInputFieldIconClickListeners() {
        binding.textInputQuantity.apply {
            /*Minus Icon*/
            setEndIconOnClickListener { viewModel.onEvent(ProductCreateFormEvent.QuantityDecreased) }
            /*Plus Icon*/
            setStartIconOnClickListener { viewModel.onEvent(ProductCreateFormEvent.QuantityIncreased) }
        }

        binding.textInputBarcode.setEndIconOnClickListener {
            viewModel.onEvent(
                ProductCreateFormEvent.StartScanBarcode
            )
        }

        binding.textInputDatePurchase.setEndIconOnClickListener {
            showDatePicker { date -> }
        }
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
            filters = arrayOf(NoLessThanZeroFilter)
        }
        binding.editTextPricePerUnit.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.PricePerUnitChanged(it.toString()))
        }
        binding.editTextDatePurchase.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.DatePurchaseChanged(it.toString()))
        }
        binding.editTextMinStockLevel.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.MinStockLevelChanged(it.toString()))
        }
        binding.autoCompleteTextViewCategory.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.CategoryChanged(it.toString()))
        }
        binding.editTextTags.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.TagsChanged(emptyList()))
        }
        binding.editTextDescription.doAfterTextChanged {
            viewModel.onEvent(ProductCreateFormEvent.DescriptionChanged(it.toString()))
        }
    }

    private fun setupAutoCompleteTextViewCategory(categories: List<ProductCategory>) {
        (binding.textInputCategory.editText as? MaterialAutoCompleteTextView)?.apply {
            setAdapter(setupCategoryAdapter(categories))
            /*setOnItemClickListener { _, _, position, _ ->
                val selectedItem = enhancedCategories[position]
                if (selectedItem.name == newCategory.name) {
                    showCategoryCreationDialog()
                    text = null
                }
            }*/
        }
        /* binding.editTextTags.doAfterTextChanged { text ->
             text?.let {
                 // Get the position of the last space, meaning the beginning of the latest word
                 val startIndex = it.lastIndexOf(" ") + 1

                 if (startIndex < it.length) {
                     // Extract the last word
                     val lastWord = it.substring(startIndex)

                     // Create a ChipDrawable for the last word
                     val chip = ChipDrawable.createFromResource(requireContext(), R.xml.chip)
                     chip.text = lastWord
                     chip.setBounds(0, 0, chip.intrinsicWidth, chip.intrinsicHeight)

                     // Apply ImageSpan only to the last word
                     val span = ImageSpan(chip)
                     it.setSpan(span, startIndex, it.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                 }
             }
         }*/
    }

    private fun showCreateCategoryDialog() {
        showDialog(
            title = "Create new category",
            iconRes = R.drawable.ic_category_add,
            theme = style.ThemeOverlay_Material3_MaterialAlertDialog_Centered,
            viewLayoutResId = R.layout.create_new_category_layout,
            positiveButtonText = "Create",
            positiveButtonAction = {
                viewModel.createCategory("Nigga")
            }
        )
    }

    private fun showEditCategoryDialog() {
        showDialog(
            title = "Edit category",
            iconRes = R.drawable.ic_category_add,
            theme = style.ThemeOverlay_Material3_MaterialAlertDialog_Centered,
            viewLayoutResId = R.layout.create_new_category_layout,
            positiveButtonText = "Save",
            positiveButtonAction = {
                viewModel.createCategory("Nigga")
            }
        )
    }

    private fun setupCategoryAdapter(categories: List<ProductCategory>): CategoryArrayAdapter =
        CategoryArrayAdapter(
            requireContext(),
            R.layout.category_item_list_autocomplete,
            categories.toTypedArray()
        ).apply {
            setOnEditClickListener { item ->
                showEditCategoryDialog()
            }
            setOnDeleteClickListener { item ->
                viewModel.deleteCategory(item)
            }
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

    private fun updateFormUIState(uiState: ProductCreateFormState) {
        binding.textInputName.error = uiState.nameError
        binding.textInputBarcode.error = uiState.barcodeError
        binding.textInputPricePerUnit.error = uiState.pricePerUnitError
        binding.textInputCategory.error = uiState.categoryError
        binding.textInputDatePurchase.error = uiState.datePurchaseError
        binding.textInputMinStockLevel.error = uiState.minStockLevelError

        binding.editTextQuantity.setText(uiState.quantity.toString())
        binding.textViewCreateProductTotalPrice.text =
            requireContext().getString(R.string.text_total_price, uiState.totalPrice)
    }
}

