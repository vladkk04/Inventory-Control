package com.example.bachelorwork.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.CustomInputLayoutTagsBinding
import com.google.android.material.chip.Chip

class CustomInputLayoutTags(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var _binding: CustomInputLayoutTagsBinding? = null
    private val binding get() = _binding!!

    init {
        _binding = CustomInputLayoutTagsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun addNewTag(tag: String) {
        val chip = createChip(tag)
        binding.flexboxLayout.addView(chip, binding.flexboxLayout.childCount - 1)
    }

    private fun createChip(tag: String): Chip =
        Chip(context).apply {
            text = tag
            isCloseIconVisible = true
            layoutParams = MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also {
                it.setMargins(10, 0, 10, 0)
            }
            chipIcon = ContextCompat.getDrawable(context, R.drawable.ic_tag)
            setOnCloseIconClickListener {
                binding.flexboxLayout.removeView(this)
            }
        }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _binding = null
    }
}