package com.example.inventorycotrol.ui.views.inputs

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.inventorycotrol.R
import com.example.inventorycotrol.databinding.CustomInputDropdownCategoryBinding
import com.example.inventorycotrol.databinding.DialogViewManageCategoryBinding
import com.example.inventorycotrol.domain.model.category.ProductCategory
import com.example.inventorycotrol.domain.model.category.ProductCategoryRequest
import com.example.inventorycotrol.ui.common.AppDialogs
import com.example.inventorycotrol.ui.common.adapters.CategoryArrayAdapter
import com.example.inventorycotrol.ui.common.base.BaseDialog
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomCategoryInputDropdown @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputLayout(context, attrs) {

    private val binding: CustomInputDropdownCategoryBinding =
        CustomInputDropdownCategoryBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private val context = binding.root.context

    private val viewModel by lazy { findViewTreeViewModelStoreOwner()?.let { ViewModelProvider(it).get<CategoriesViewModel>() } }

    private var listener: OnItemClickListener? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            viewModel?.uiState?.collectLatest { uiState ->
                if (uiState.currentCategory != null) {
                    binding.autoCompleteTextViewCategory.setText(uiState.currentCategory.name, false)
                    binding.autoCompleteTextViewCategory.dismissDropDown()
                }
                setupAutoCompleteTextViewCategory(uiState.categories)
            }
        }
    }

    private fun setupAutoCompleteTextViewCategory(categories: List<ProductCategory>) {
        (binding.autoCompleteTextViewCategory as? MaterialAutoCompleteTextView)?.setAdapter(
            setupCategoryAdapter(categories)
        )
    }

    fun setOnClickItemListener(itemClickListener: OnItemClickListener) {
        listener = itemClickListener
    }

    fun setErrorMessage(message: String?) {
        binding.textInputLayoutCategory.error = message
    }

    private fun setupCategoryAdapter(categories: List<ProductCategory>): CategoryArrayAdapter {
        return CategoryArrayAdapter(
            context,
            categories.toMutableList()
        ).apply {
            setOnClickItemListener { item ->
                listener?.onClick(item)
                viewModel?.selectCategory(item)
            }
            setOnCreateNewCategory { createNewCategoryDialog().show() }
            setOnEditClickListener { category -> createEditCategoryDialog(category).show() }
            setOnDeleteClickListener { item ->
                AppDialogs.createDeleteDialog(
                    context,
                    message = context.getString(R.string.text_delete_category_confirm, item.name),
                    deleteItemTitle = item.name
                ) {
                    viewModel?.deleteCategory(item.id)
                }.show()

            }
        }
    }

    fun interface OnItemClickListener {
        fun onClick(item: ProductCategory)
    }

    private fun createNewCategoryDialog() = object : BaseDialog(context) {
        val binding = DialogViewManageCategoryBinding.inflate(LayoutInflater.from(context))

        override val config: DialogConfig = DialogConfig(
            view = binding.root,
            theme = com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered,
            positiveButtonAction = {
                viewModel?.createCategory(ProductCategoryRequest(name = binding.editTextCategory.text.toString().trim()))
            },
            title = "Create category",
            iconRes = R.drawable.ic_category_add,
            positiveButtonText = "Create"
        )

        override fun buildDialog(): AlertDialog {
            return super.buildDialog().apply {
                setOnShowListener {
                    val button = getButton(DialogInterface.BUTTON_POSITIVE)
                    button.isEnabled = binding.editTextCategory.text.toString().isNotEmpty()
                    binding.editTextCategory.doAfterTextChanged {
                        button.isEnabled = it.toString().isNotEmpty()
                    }
                }
            }
        }
    }

    private fun createEditCategoryDialog(
        category: ProductCategory,
    ): BaseDialog {
        val binding = DialogViewManageCategoryBinding.inflate(LayoutInflater.from(context))

        return object : BaseDialog(context) {
            override val config: DialogConfig = DialogConfig(
                view = binding.root,
                theme = com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered,
                positiveButtonAction = {
                    viewModel?.updateCategory(categoryId = category.id, ProductCategoryRequest(binding.editTextCategory.text.toString().trim())) },
                title = "Edit category",
                iconRes = R.drawable.ic_edit,
                positiveButtonText = "Save"
            )

            override fun buildDialog(): AlertDialog {
                return super.buildDialog().apply {
                    setOnShowListener {
                        val button = getButton(DialogInterface.BUTTON_POSITIVE)
                        val categoryText = binding.editTextCategory.text.toString()

                        button.isEnabled =
                            categoryText.isNotEmpty() && category.name != categoryText

                        binding.editTextCategory.setText(category.name)
                        binding.editTextCategory.doAfterTextChanged {
                            button.isEnabled = it.toString().isNotEmpty() && category.name != it.toString()
                        }
                    }
                }
            }
        }
    }

    fun setDefaultCategory(category: String) {
        binding.autoCompleteTextViewCategory.setText(category, false)
        binding.autoCompleteTextViewCategory.dismissDropDown()
    }
}