package com.example.bachelorwork.ui.utils.dialogs

import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.CustomDialogViewContentCreateCategoryBinding
import com.example.bachelorwork.domain.model.product.ProductCategory
import com.example.bachelorwork.ui.common.base.BaseDialog

enum class CategoryDialogType {
    EDIT,
    CREATE
}

fun Fragment.createCategoryDialog(
    category: ProductCategory? = null,
    type: CategoryDialogType,
    onPositiveButtonClick: (category: ProductCategory) -> Unit = {},
) = object : BaseDialog(requireContext()) {
    private val binding = CustomDialogViewContentCreateCategoryBinding.inflate(LayoutInflater.from(context))

    private val defaultConfig = DialogConfig(
        view = binding.root,
        theme = com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered,
        positiveButtonAction = {
            when (type) {
                CategoryDialogType.EDIT -> {
                    if (category != null) {
                        onPositiveButtonClick(
                            category.copy(name = binding.editTextCategory.text.toString())
                        )
                    }
                }
                CategoryDialogType.CREATE -> {
                    onPositiveButtonClick(
                        ProductCategory(name = binding.editTextCategory.text.toString())
                    )
                }
            }
        }
    )

    override val config: DialogConfig
        get() = when (type) {
            CategoryDialogType.EDIT -> defaultConfig.copy(
                title = "Edit category",
                iconRes = R.drawable.ic_edit,
                positiveButtonText = "Save",
            )

            CategoryDialogType.CREATE -> defaultConfig.copy(
                title = "Create category",
                iconRes = R.drawable.ic_category_add,
                positiveButtonText = "Create"
            )
        }

    override fun buildDialog(): AlertDialog {
        return super.buildDialog().apply {
            setOnShowListener {
                binding.editTextCategory.setText(category?.name)
                binding.editTextCategory.doAfterTextChanged {
                    getButton(DialogInterface.BUTTON_POSITIVE).isEnabled =
                        it.toString().isNotEmpty()
                }
            }
        }
    }
}