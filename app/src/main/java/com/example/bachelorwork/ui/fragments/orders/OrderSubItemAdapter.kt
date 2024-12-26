package com.example.bachelorwork.ui.fragments.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.OrderItemInnerBinding
import com.example.bachelorwork.domain.model.order.OrderSubItemProduct

class OrderSubItemAdapter : RecyclerView.Adapter<OrderSubItemAdapter.ViewHolder>() {

    private var list: List<OrderSubItemProduct> = emptyList()

    fun submitList(list: List<OrderSubItemProduct>) {
        this.list = list
    }

    inner class ViewHolder(val binding: OrderItemInnerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderSubItemProduct) {
            binding.textViewName.text = item.name
            binding.textViewQuantity.text = binding.root.context.getString(R.string.text_product_item_pcs, item.quantity)
            binding.textViewPrice.text =  binding.root.context.getString(R.string.text_price, item.price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OrderItemInnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}