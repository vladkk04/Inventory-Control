package com.example.inventorycotrol.ui.views.inputs

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.widget.doAfterTextChanged
import com.example.inventorycotrol.databinding.CustomTextInputLayoutTagsBinding
import com.example.inventorycotrol.domain.model.product.ProductTag
import com.example.inventorycotrol.ui.views.createProductTagChip
import com.google.android.material.chip.Chip

class CustomTextInputLayoutTags @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var binding: CustomTextInputLayoutTagsBinding =
        CustomTextInputLayoutTagsBinding.inflate(LayoutInflater.from(context), this, true)

    private var onCloseIconClickListener: OnCloseIconClickListener? = null

    private var onTextChangeListener: ((List<ProductTag>) -> Unit)? = null

    val tags: MutableList<ProductTag> = mutableListOf()

    init {
        binding.editTextTags.doAfterTextChanged {
            val tagName = it.toString()
            if(it.isNullOrEmpty()) return@doAfterTextChanged

            if (it.isNotEmpty() && it.last() == ' ') {
                addChip(ProductTag(tagName.dropLast(1)))
                onTextChangeListener?.invoke(tags.toList())
            }
        }

        binding.editTextTags.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.editTextTags.text?.isNotEmpty() == true) {
                addChip(ProductTag(binding.editTextTags.text.toString()))
            }
        }
    }

    fun setCustomHelperText(hint: String) {
        binding.customHint.text = hint
        binding.customHint.visibility = VISIBLE
    }

    fun setOnTextChangeListener(listener: (List<ProductTag>) -> Unit) {
        this.onTextChangeListener = listener
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
