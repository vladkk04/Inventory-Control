package com.example.bachelorwork.ui.fragments.productList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.ProductItemGridBinding
import com.example.bachelorwork.databinding.ProductItemRowBinding
import com.example.bachelorwork.domain.model.product.ProductViewType
import com.example.bachelorwork.ui.model.productList.ProductListUI


class ProductListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, ProductListUIDiffUtilCallbacks)
    private val currentList get() = asyncListDiffer.currentList

    private var viewType = ProductViewType.ROW


    fun setViewType(viewType: ProductViewType) {
        if (this.viewType == viewType) return
        this.viewType = viewType
        notifyItemRangeChanged(0, currentList.size)
    }

    fun submitList(list: List<ProductListUI>, onComplete: (() -> Unit)? = null) {
        if (list.isEmpty()) return
        asyncListDiffer.submitList(list) { onComplete?.invoke() }
    }

    private inner class ViewHolderRow(private val binding: ProductItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListUI) {
            val context = binding.root.context

            with(binding) {
                Glide.with(context)
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_image)
                    .into(shapeableImageView)

                textViewName.text = item.name
                textViewPrice.text = context.getString(R.string.product_item_price, item.price)
                textViewUpcCode.text =
                    context.getString(R.string.product_item_upc_code, item.barcode)
                textViewQuantity.text = item.quantity.toString()
            }
        }
    }

    private inner class ViewHolderGrid(private val binding: ProductItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductListUI) {
            val context = binding.root.context

            with(binding) {
                Glide.with(context)
                    .load(item.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_image)
                    .into(shapeableImageView)

                textViewName.text = item.name
                textViewPrice.text = context.getString(R.string.product_item_price, item.price)
                textViewUpcCode.text =
                    context.getString(R.string.product_item_upc_code, item.barcode)
                textViewQuantity.text = item.quantity.toString()
            }
        }
    }

    override fun getItemViewType(position: Int): Int = viewType.ordinal

    override fun getItemId(position: Int): Long = currentList[position].hashCode().toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ProductViewType.ROW.ordinal -> {
                ViewHolderRow(
                    ProductItemRowBinding.inflate(inflater, parent, false)
                )
            }

            ProductViewType.GRID.ordinal -> {
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
            ProductViewType.ROW.ordinal -> (holder as ViewHolderRow).bind(currentList[position])
            ProductViewType.GRID.ordinal -> (holder as ViewHolderGrid).bind(currentList[position])
        }
    }



    override fun getItemCount(): Int = currentList.size

}