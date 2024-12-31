package com.example.bachelorwork.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.bachelorwork.databinding.CustomInputLayoutCategoriesBinding
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.ui.common.adapters.CategoryArrayAdapter
import com.example.bachelorwork.ui.utils.dialogs.CategoryDialogType
import com.example.bachelorwork.ui.utils.dialogs.createCategoryDialog
import com.example.bachelorwork.ui.utils.dialogs.createDeleteDialog
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomInputLayoutCategories @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputLayout(context, attrs) {

    private val binding: CustomInputLayoutCategoriesBinding =
        CustomInputLayoutCategoriesBinding.inflate(
            LayoutInflater.from(context), this, true
        )

    private val context = binding.root.context

    private val viewModel by lazy { findViewTreeViewModelStoreOwner()?.let { ViewModelProvider(it).get<CategoriesViewModel>() } }

    private lateinit var listener: OnItemClickListener

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
            viewModel?.uiState?.collectLatest { uiState ->
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

    private fun setupCategoryAdapter(categories: List<ProductCategory>): CategoryArrayAdapter {
        return CategoryArrayAdapter(
            context,
            categories.toMutableList()
        ).apply {
            setOnClickItemListener { item ->
                listener.onClick(item)
                binding.autoCompleteTextViewCategory.setText(item.name, false)
                binding.autoCompleteTextViewCategory.dismissDropDown()
            }
            setOnCreateNewCategory {
                createCategoryDialog(context = context, type = CategoryDialogType.CREATE) {
                    viewModel?.createCategory(it)
                }.show()
            }
            setOnEditClickListener { item ->
                createCategoryDialog(context = context, item, CategoryDialogType.EDIT) {
                    viewModel?.updateCategory(it)
                }.show()
            }
            setOnDeleteClickListener { item ->
                createDeleteDialog(context, "category \"${item.name}\"") {
                    viewModel?.deleteCategory(item)
                }.show()
            }
        }
    }

    fun interface OnItemClickListener {
        fun onClick(item: ProductCategory)
    }
}