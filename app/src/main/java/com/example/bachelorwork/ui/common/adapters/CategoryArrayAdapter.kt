package com.example.bachelorwork.ui.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.bachelorwork.databinding.CustomCategoryDropdownItemBinding
import com.example.bachelorwork.domain.model.category.ProductCategory

class CategoryArrayAdapter(
    context: Context,
    items: MutableList<ProductCategory>
) : ArrayAdapter<ProductCategory>(context, 0, items) {

    private var onCreateNewCategoryClickListener: OnCreateNewCategoryClickListener? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var onEditClickListener: OnEditClickListener? = null
    private var onClickItemListener: OnItemClickListener? = null

    init {
        insert(ProductCategory("", name = "Create new category"), 0)
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

    private fun bind(binding: CustomCategoryDropdownItemBinding, item: ProductCategory) {
        binding.textViewCategory.text = item.name

        if (item.name == "Create new category") {
            binding.checkBoxEdit.visibility = View.GONE
            binding.checkBoxDelete.visibility = View.GONE
            binding.textViewCategory.setOnClickListener {
                onCreateNewCategoryClickListener?.onClick()
            }
        } else {
            binding.checkBoxEdit.visibility = View.VISIBLE
            binding.checkBoxDelete.visibility = View.VISIBLE

            binding.textViewCategory.setOnClickListener {
                onClickItemListener?.onItemClick(item)
            }
            binding.checkBoxEdit.setOnClickListener {
                onEditClickListener?.onItemClick(item)
            }
            binding.checkBoxDelete.setOnClickListener {
                onDeleteClickListener?.onItemClick(item)
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: CustomCategoryDropdownItemBinding = if (convertView != null) {
            CustomCategoryDropdownItemBinding.bind(convertView)
        } else {
            CustomCategoryDropdownItemBinding.inflate(LayoutInflater.from(context), parent, false)
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
