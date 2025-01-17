package com.example.bachelorwork.ui.fragments.warehouse.productList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.ProductItemGridBinding
import com.example.bachelorwork.databinding.ProductItemRowBinding
import com.example.bachelorwork.domain.model.product.ProductDisplayMode
import com.example.bachelorwork.ui.model.product.list.ProductUI
import java.util.Locale


class ProductListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, ProductListUIDiffUtilCallbacks)
    private val currentList get() = asyncListDiffer.currentList

    private var onItemClickListener: OnItemClickListener? = null

    private var viewType = ProductDisplayMode.ROW

    fun setViewType(viewType: ProductDisplayMode) {
        if (this.viewType == viewType) return
        this.viewType = viewType
        //For smoothly change animation
        //notifyItemRangeChanged(0, currentList.size)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun submitList(list: List<ProductUI>, onComplete: (() -> Unit)? = null) {
        asyncListDiffer.submitList(list) { onComplete?.invoke() }
    }

    private inner class ViewHolderRow(private val binding: ProductItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductUI) {
            val context = binding.root.context

            with(binding) {
                Glide.with(context)
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_image)
                    .into(shapeableImageView)

                textViewName.text = item.name
                textViewBarcode.text = context.getString(R.string.text_product_item_barcode, item.barcode)
                textViewQuantity.text = String.format(Locale.getDefault(), item.quantity.toString())
                textViewUnit.text = item.unit.name
            }

            binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(position = item.id)
            }
        }
    }

    private inner class ViewHolderGrid(private val binding: ProductItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductUI) {
            val context = binding.root.context

            with(binding) {
                Glide.with(context)
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_image)
                    .into(shapeableImageView)

                textViewName.text = item.name
                textViewBarcode.text = context.getString(R.string.text_product_item_barcode, item.barcode)
                textViewQuantity.text = String.format(Locale.getDefault(), item.quantity.toString())
                textViewUnit.text = item.unit.name
            }

            binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(position = item.id)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = viewType.ordinal

    override fun getItemId(position: Int): Long = currentList[position].hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ProductDisplayMode.ROW.ordinal -> {
                ViewHolderRow(
                    ProductItemRowBinding.inflate(inflater, parent, false)
                )
            }

            ProductDisplayMode.GRID.ordinal -> {
                ViewHolderGrid(
                    ProductItemGridBinding.inflate(inflater, parent, false)
                )
            }

            else -> {
                ViewHolderRow(
                    ProductItemRowBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ProductDisplayMode.ROW.ordinal -> (holder as ViewHolderRow).bind(currentList[position])
            ProductDisplayMode.GRID.ordinal -> (holder as ViewHolderGrid).bind(currentList[position])
        }
    }

    override fun getItemCount(): Int = currentList.size

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

}