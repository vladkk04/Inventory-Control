package com.example.bachelorwork.ui.fragments.orders

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bachelorwork.R
import com.example.bachelorwork.databinding.OrderItemOuterBinding
import com.example.bachelorwork.domain.model.order.Order
import com.example.bachelorwork.ui.utils.StateListDrawableFactory

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    private val asyncListDiffer = AsyncListDiffer(this, OrdersDiffUtilCallbacks)

    fun submitList(list: List<Order>) {
        if (list.isEmpty()) return
        asyncListDiffer.submitList(list)
    }

    inner class ViewHolder(val binding: OrderItemOuterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order) {
            binding.textViewOrderId.text =
                binding.root.context.getString(R.string.text_order_id, item.id)
            binding.textViewOrderedDate.text =
                binding.root.context.getString(R.string.text_order_date_timestamp, item.date)
            binding.textViewTotal.text =
                binding.root.context.getString(R.string.text_order_total, item.total)

            binding.checkBoxToggle.apply {
                buttonDrawable = StateListDrawableFactory.createCheckedDrawable(
                    binding.root.context,
                    R.drawable.ic_keyboard_arrow_up,
                    R.drawable.ic_keyboard_arrow_down
                )
                setOnCheckedChangeListener { _, isChecked ->
                    binding.recyclerViewSubitems.visibility =
                        if (isChecked) View.VISIBLE else View.GONE
                }
            }
            val params = binding.recyclerViewSubitems.layoutParams

            params.height = if (item.items.size > 4) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200F,
                binding.root.context.resources.displayMetrics
            ).toInt() else ViewGroup.LayoutParams.WRAP_CONTENT

            binding.recyclerViewSubitems.apply {
                setHasFixedSize(true)
                setRecycledViewPool(RecyclerView.RecycledViewPool())
                adapter = OrderSubItemAdapter().apply {
                    submitList(item.items)
                }
                layoutParams = params
                layoutManager = LinearLayoutManager(binding.root.context)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            OrderItemOuterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }
}