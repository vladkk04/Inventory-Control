package com.example.bachelorwork.ui.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.bachelorwork.databinding.CustomAutocompleteCategoryItemBinding

import com.example.bachelorwork.domain.model.ProductCategory

//TODO Fix last item, when i clicked again on it (means second time) appears create new dialog
class CategoryArrayAdapter(
    context: Context,
    items: List<ProductCategory>
) : ArrayAdapter<ProductCategory>(context, 0, items) {

    var isShowCreateOption = true

    init {
        if (isShowCreateOption) { insert(ProductCategory("Create new category"), 0) }
    }

    private val originalItems: List<ProductCategory> = items

    private var onCreateNewCategoryClickListener: OnCreateNewCategoryClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onEditClickListener: OnEditClickListener? = null

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: OnEditClickListener) {
        onEditClickListener = listener
    }

    fun setOnCreateNewCategory(listener: OnCreateNewCategoryClickListener) {
        onCreateNewCategoryClickListener = listener
    }

    private fun bind(binding: CustomAutocompleteCategoryItemBinding, item: ProductCategory) {
        binding.textViewCategory.text = item.name

        if (item.name == "Create new category") {
            binding.checkboxEdit.visibility = View.GONE
            binding.checkboxDelete.visibility = View.GONE
            binding.textViewCategory.setOnClickListener {
                onCreateNewCategoryClickListener?.onClick()
            }
        } else {
            binding.checkboxEdit.visibility = View.VISIBLE
            binding.checkboxDelete.visibility = View.VISIBLE
            binding.checkboxEdit.setOnClickListener {
                onEditClickListener?.onClick(item)
            }
            binding.checkboxDelete.setOnClickListener {
                onDeleteClickListener?.onClick(item)
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: CustomAutocompleteCategoryItemBinding = if (convertView != null) {
            CustomAutocompleteCategoryItemBinding.bind(convertView)
        } else {
            CustomAutocompleteCategoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        }

        val currentItem = getItem(position) ?: return binding.root
        bind(binding, currentItem)

        return binding.root
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val suggestion = mutableListOf<ProductCategory>()

                if (constraint.isNullOrEmpty()) {
                    suggestion.addAll(originalItems) // Use the original list for suggestions
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    for (category in originalItems) {
                        if (category.name.lowercase().contains(filterPattern)) {
                            suggestion.add(category)
                        }
                    }
                }

                results.values = suggestion
                results.count = suggestion.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                results?.values?.let { values ->
                    addAll(values as List<ProductCategory>)
                }
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as ProductCategory).name
            }
        }
    }

    fun interface OnCreateNewCategoryClickListener {
        fun onClick()
    }

    fun interface OnDeleteClickListener {
        fun onClick(item: ProductCategory)
    }

    fun interface OnEditClickListener {
        fun onClick(item: ProductCategory)
    }
}
