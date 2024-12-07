package com.example.bachelorwork.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import com.example.bachelorwork.databinding.CustomInputLayoutTagsBinding
import com.example.bachelorwork.domain.model.product.ProductTag
import com.google.android.material.chip.Chip

class CustomInputLayoutTags @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs), TextView.OnEditorActionListener {

    private var binding: CustomInputLayoutTagsBinding =
        CustomInputLayoutTagsBinding.inflate(LayoutInflater.from(context), this, true)

    private var onCloseIconClickListener: OnCloseIconClickListener? = null

    private val tags: MutableList<ProductTag> = mutableListOf()

    init {
        binding.editTextTags.setOnEditorActionListener(this)
    }

    fun onTextChangeListener(onChange: (tags: List<ProductTag>) -> Unit) {
        binding.editTextTags.doAfterTextChanged {
            val tagName = it.toString()
            if (!it.isNullOrEmpty() && it.last() == ' ') {
                addChip(ProductTag(tagName.dropLast(1)))
                onChange.invoke(tags.toList())
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

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        v?.text?.toString()?.takeIf { it.isNotBlank() }?.let { addChip(ProductTag(it)) }
        return true
    }

    fun interface OnCloseIconClickListener {
        fun onCloseIconClickListener(chip: Chip)
    }
}
