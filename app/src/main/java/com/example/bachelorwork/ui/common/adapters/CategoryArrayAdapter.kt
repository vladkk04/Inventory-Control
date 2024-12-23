package com.example.bachelorwork.ui.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.bachelorwork.databinding.CustomAutocompleteCategoryItemBinding
import com.example.bachelorwork.domain.model.product.ProductCategory

class CategoryArrayAdapter(
    context: Context,
    items: MutableList<ProductCategory>
) : ArrayAdapter<ProductCategory>(context, 0, items) {

    private var onCreateNewCategoryClickListener: OnCreateNewCategoryClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onEditClickListener: OnEditClickListener? = null
    private var onClickItemListener: OnItemClickListener? = null

    init {
        insert(ProductCategory(name = "Create new category"), 0)
    }

    fun setOnCreateNewCategory(listener: OnCreateNewCategoryClickListener) {
        onCreateNewCategoryClickListener = listener
    }

    fun setOnClickItemListener(listener: OnItemClickListener) {
        onClickItemListener = listener
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }

    fun setOnEditClickListener(listener: OnEditClickListener) {
        onEditClickListener = listener
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

            binding.textViewCategory.setOnClickListener {
                onClickItemListener?.onItemClick(item)
            }
            binding.checkboxEdit.setOnClickListener {
                onEditClickListener?.onItemClick(item)
            }
            binding.checkboxDelete.setOnClickListener {
                onDeleteClickListener?.onItemClick(item)
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

    fun interface OnItemClickListener {
        fun onItemClick(item: ProductCategory)
    }

    fun interface OnCreateNewCategoryClickListener {
        fun onClick()
    }

    fun interface OnDeleteClickListener: OnItemClickListener

    fun interface OnEditClickListener: OnItemClickListener
}
