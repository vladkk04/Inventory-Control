package com.example.bachelorwork.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.widget.doAfterTextChanged
import com.example.bachelorwork.databinding.CustomInputLayoutTagsBinding
import com.example.bachelorwork.domain.model.product.ProductTag
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputLayout

// TODO Refactoring Code for Input layout
class CustomInputLayoutTags @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputLayout(context, attrs) {

    private var binding: CustomInputLayoutTagsBinding =
        CustomInputLayoutTagsBinding.inflate(LayoutInflater.from(context), this, true)

    private var onCloseIconClickListener: OnCloseIconClickListener? = null

    private val tags: MutableList<ProductTag> = mutableListOf()


    fun onTextChangeListener(onChange: (tags: List<ProductTag>) -> Unit) {
        binding.editTextTags.doAfterTextChanged {
            val tagName = it.toString()
            if(it.isNullOrEmpty()) return@doAfterTextChanged

            if (it.isNotEmpty() && it.last() == ' ') {
                addChip(ProductTag(tagName.dropLast(1)))
                onChange.invoke(tags.toList())
            }
        }

        binding.editTextTags.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.editTextTags.text?.isNotEmpty() == true) {
                addChip(ProductTag(binding.editTextTags.text.toString()))
            }
        }
    }

    fun setOnCloseIconClickListener(listener: OnCloseIconClickListener) {
        onCloseIconClickListener = listener
    }

    private fun removeChip(chip: Chip) {
        binding.flexboxLayout.removeView(chip)
    }

    fun addTags(vararg tags: ProductTag) {
        tags.forEach { addChip(it) }
    }

    private fun addChip(tag: ProductTag) {
        if (tag.name.isBlank() || tags.contains(tag)) return
        tags.add(tag)
        binding.editTextTags.text?.clear()
        binding.flexboxLayout.addView(createChip(tag), binding.flexboxLayout.childCount - 1)
    }

    private fun createChip(tag: ProductTag): Chip {
        return createProductTagChip(context, tag).apply {
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                onCloseIconClickListener?.onCloseIconClickListener(this) ?: run {
                    tags.remove(tag)
                    removeChip(this)
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        tags.clear()
    }

    fun interface OnCloseIconClickListener {
        fun onCloseIconClickListener(chip: Chip)
    }
}
