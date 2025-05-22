package com.example.inventorycotrol.ui.fragments.warehouse.productList

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.inventorycotrol.R
import com.example.inventorycotrol.data.constants.AppConstants
import com.example.inventorycotrol.data.remote.dto.ThresholdSettings
import com.example.inventorycotrol.databinding.ProductGridItemBinding
import com.example.inventorycotrol.databinding.ProductRowItemBinding
import com.example.inventorycotrol.domain.model.product.Product
import com.example.inventorycotrol.domain.model.product.ProductViewDisplayMode

class ProductListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private object ProductListUIDiffUtilCallbacks : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }

    private val asyncListDiffer = AsyncListDiffer(this, ProductListUIDiffUtilCallbacks)
    private val currentList get() = asyncListDiffer.currentList

    private var onItemClickListener: OnItemClickListener? = null

    private var viewType = ProductViewDisplayMode.ROW

    private var thresholdSettings: ThresholdSettings? = null

    fun setViewType(viewType: ProductViewDisplayMode) {
        if (this.viewType == viewType) return
        this.viewType = viewType
        //For smoothly change animation
        notifyItemRangeChanged(0, currentList.size)
    }

    fun setupThresholdSettings(settings: ThresholdSettings) {
        thresholdSettings = settings
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun submitList(list: List<Product>, onComplete: (() -> Unit)? = null) {
        asyncListDiffer.submitList(list) { onComplete?.invoke() }
    }

    private inner class ViewHolderRow(private val binding: ProductRowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            val context = binding.root.context

            with(binding) {
                item.imageUrl?.let {
                    Glide.with(context)
                        .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it}")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.ic_image)
                        .centerCrop()
                        .into(imageView)
                }

                textViewOutOfStock.isVisible = item.quantity == 0.00
                binding.cardView.alpha = if (textViewOutOfStock.isVisible) 0.33f else 1f

                val color = when {
                    item.quantity >= (item.minStockLevel * thresholdSettings!!.normalThresholdPercentage) / 100 ->
                        R.color.colorMinStockLevelNormalLevel

                    item.quantity <= (item.minStockLevel * thresholdSettings!!.normalThresholdPercentage) / 100 &&
                            item.quantity >= (item.minStockLevel * thresholdSettings!!.mediumThresholdPercentage) / 100 ->
                        R.color.colorMinStockLevelNormalLevel

                    item.quantity >= (item.minStockLevel * thresholdSettings!!.mediumThresholdPercentage) / 100
                            || item.quantity > (item.minStockLevel * thresholdSettings!!.criticalThresholdPercentage) / 100 ->
                        R.color.colorMinStockLevelMediumLevel

                    else -> R.color.colorMinStockLevelLowLevel
                }

                textViewQuantity.backgroundTintList =
                    ColorStateList.valueOf(context.getColor(color))

                textViewName.text = item.name
                textViewBarcode.text =
                    context.getString(R.string.text_product_item_barcode, item.barcode)
                textViewCategory.text =
                    context.getString(R.string.text_product_item_category, item.categoryName ?: "Unidentified")
                textViewMinimumStockLevel.text = context.getString(
                    R.string.text_product_item_min_stock_value,
                    item.minStockLevel
                )
                textViewQuantity.text = context.getString(
                    R.string.text_quantity_unit_value,
                    item.quantity,
                    item.unit.name
                )
            }

            binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(position = item.id)
            }
        }
    }

    private inner class ViewHolderGrid(private val binding: ProductGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            val context = binding.root.context

            with(binding) {
                item.imageUrl?.let {
                    Glide.with(context)
                        .load("${AppConstants.BASE_URL_CLOUD_FRONT}${it}")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.ic_image)
                        .centerCrop()
                        .into(imageView)
                }

                textViewOutOfStock.isVisible = item.quantity == 0.00
                binding.cardView.alpha = if (textViewOutOfStock.isVisible) 0.33f else 1f

                val color = when {
                    item.quantity >= item.minStockLevel -> R.color.colorMinStockLevelNormalLevel
                    item.quantity > item.minStockLevel * 0.25 -> R.color.colorMinStockLevelMediumLevel
                    else -> R.color.colorMinStockLevelLowLevel
                }

                layoutQuantity.setBackgroundColor(context.getColor(color))

                textViewName.text = item.name
                textViewBarcode.text =
                    context.getString(R.string.text_product_item_barcode, item.barcode)
                textViewCategory.text =
                    context.getString(R.string.text_product_item_category, item.categoryName ?: "Unidentified")
                textViewMinimumStockLevel.text = context.getString(
                    R.string.text_product_item_min_stock_value,
                    item.minStockLevel
                )
                textViewQuantity.text = context.getString(
                    R.string.text_quantity_unit_value,
                    item.quantity,
                    item.unit.name
                )
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
            ProductViewDisplayMode.ROW.ordinal -> {
                ViewHolderRow(
                    ProductRowItemBinding.inflate(inflater, parent, false)
                )
            }

            ProductViewDisplayMode.GRID.ordinal -> {
                ViewHolderGrid(
                    ProductGridItemBinding.inflate(inflater, parent, false)
                )
            }

            else -> {
                ViewHolderRow(
                    ProductRowItemBinding.inflate(inflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ProductViewDisplayMode.ROW.ordinal -> (holder as ViewHolderRow).bind(currentList[position])
            ProductViewDisplayMode.GRID.ordinal -> (holder as ViewHolderGrid).bind(currentList[position])
        }
    }

    override fun getItemCount(): Int = currentList.size

    fun interface OnItemClickListener {
        fun onItemClick(position: String)
    }

}
