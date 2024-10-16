package com.example.bachelorwork.ui.productCreate

import android.text.Spanned
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.ModalBottomSheetProductCreateBinding
import com.example.bachelorwork.domain.model.ProductCreateUIState
import com.example.bachelorwork.ui.common.BaseBottomSheetDialogFragment
import com.example.bachelorwork.ui.common.showDatePicker
import com.example.bachelorwork.ui.common.showDialog
import com.example.bachelorwork.ui.common.showDiscardDialog
import com.example.bachelorwork.ui.utils.inputFilters.NoLessThanZeroFilter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.ChipDrawable
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

    override val onBackPressedDispatcher: OnBackPressedCallback
        get() = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDiscardDialog(
                    positiveButtonAction = { dismiss() },
                    negativeButtonAction = { }
                )
            }
        }

    override fun setupToolbar(): MaterialToolbar? = binding.bottomSheetToolbar

    override fun setupViews() {
        setupImageViewOnClickListener()
        setupInputFields()
        setupNumberPicker()
        setupAutoCompleteTextViewCategory()
        viewLifecycleOwner.lifecycleScope.launch {
            observeUIState()
        }
    }

    /*private fun setupToolbar() {
        binding.bottomSheetToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.handleOnBackPressed()
        }
        binding.bottomSheetToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_save_new_product -> {
                    true
                }

                else -> { false }
            }
        }
    }*/

    private fun setupNumberPicker() {
        val stringValues = arrayOf("PCS", "KG", "CM", "KM", "T", "G", "BOX")

        binding.numberPickerCreateProductSelectUnit.apply {
            minValue = 0
            maxValue = stringValues.size - 1
            displayedValues = stringValues
        }.setOnScrollListener { view, _ ->
            binding.textInputPrice.hint = requireContext().getString(
                R.string.text_price_per_unit, stringValues[view.value].lowercase()
            )
        }
    }

    private fun setupImageViewOnClickListener() {
        binding.imageViewCreateProduct.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
        }
    }

    private fun setupInputFields() {
        binding.textInputQuantity.apply {
            setEndIconOnClickListener { viewModel.increaseQuantity() }
            setStartIconOnClickListener { viewModel.decreaseQuantity() }
        }

        binding.editTextQuantity.apply {
            doAfterTextChanged { text -> viewModel.setQuantity(text.toString().toIntOrNull()) }
            setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) setText(viewModel.uiState.value.quantity.toString()) }
            filters = arrayOf(NoLessThanZeroFilter)
        }

        binding.editTextPrice.apply {
            doAfterTextChanged { text -> viewModel.setPrice(text.toString().toDoubleOrNull()) }
            setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) setText(viewModel.uiState.value.price.toString()) }
        }

        binding.textInputBarcode.setEndIconOnClickListener { viewModel.startScanBarcode() }

        binding.textInputDatePurchase.setEndIconOnClickListener {
            showDatePicker { date ->
                viewModel.setDatePurchase(date)
            }
        }

        binding.textInputCategory.setStartIconOnClickListener {

        }
    }

    private fun setupAutoCompleteTextViewCategory() {
        val items = arrayOf("Create new category", "Item 2", "Item 3", "Item 4")
        (binding.textInputCategory.editText as? MaterialAutoCompleteTextView)?.let {
            it.setSimpleItems(items)
            it.setOnItemClickListener { parent, view, position, id ->
                items[position].let { item ->
                    if (item == "Create new category") {
                        showDialog(
                            title = "Create new category",
                            iconRes = R.drawable.ic_category_add,
                            theme = com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered,
                            viewLayoutResId = R.layout.view_create_new_category,
                            positiveButtonText = "Create",
                        )
                    }
                    it.text = null
                }
            }
        }
        binding.editTextTags.doAfterTextChanged { text ->
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
        }
    }

    private suspend fun observeUIState() = viewModel.uiState.collectLatest { uiState ->
        updateUIState(uiState)
    }

    private fun updateUIState(uiState: ProductCreateUIState) {
        with(uiState) {
            binding.textViewCreateProductTotalPrice.text =
                requireContext().getString(R.string.text_total_price, totalPrice)
            binding.editTextDatePurchase.setText(datePurchase)
            binding.editTextQuantity.apply {
                setText(quantity.toString())
                setSelection(quantity.toString().length)
            }
            binding.editTextBarcode.setText(barcode)
        }

    }
}

