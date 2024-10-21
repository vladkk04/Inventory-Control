package com.example.bachelorwork.ui.productCreate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.example.bachelorwork.databinding.CategoryItemListAutocompleteBinding
import com.example.bachelorwork.domain.model.ProductCategory

class CategoryArrayAdapter(
    private val context: Context,
    @LayoutRes private val viewResourceId: Int,
    private val items: Array<ProductCategory>
) : ArrayAdapter<ProductCategory>(context, viewResourceId, items) {

    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onEditClickListener: OnEditClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: OnEditClickListener) {
        onEditClickListener = listener
    }

    private fun bind(binding: CategoryItemListAutocompleteBinding, item: ProductCategory) {
        binding.textViewCategory.text = item.name
        binding.checkboxEditCategory.setOnCheckedChangeListener { _, _ ->
            onEditClickListener?.onClick(item)
        }
        binding.checkboxDeleteCategory.setOnCheckedChangeListener { _, _ ->
            onDeleteClickListener?.onClick(item)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: CategoryItemListAutocompleteBinding = if (convertView != null) {
            CategoryItemListAutocompleteBinding.bind(convertView)
        } else {
            CategoryItemListAutocompleteBinding.inflate(LayoutInflater.from(context), parent, false)
        }

        bind(binding, items[position])

        return binding.root
    }

    fun interface OnDeleteClickListener {
        fun onClick(item: ProductCategory)
    }

    fun interface OnEditClickListener {
        fun onClick(item: ProductCategory)
    }
}