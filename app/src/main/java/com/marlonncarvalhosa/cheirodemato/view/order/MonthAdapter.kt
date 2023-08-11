package com.marlonncarvalhosa.cheirodemato.view.order

import android.text.method.TextKeyListener.Capitalize
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.databinding.ItemMonthBinding
import com.marlonncarvalhosa.cheirodemato.databinding.ItemOrderBinding
import com.marlonncarvalhosa.cheirodemato.view.home.OrdersAdapter

class MonthAdapter(
    private val name: List<String>,
    private val data: List<OrderModel>
    //private val onClickListener: (OrderModel) -> Unit
) : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMonthBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            item.let {
                binding.textMonth.text = it.capitalize()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthAdapter.ViewHolder {
        val binding = ItemMonthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonthAdapter.ViewHolder, position: Int) {
        holder.bind(name[position])
        val finishList = data.filter { f -> f.monthName == name[position] }
        holder.itemView.findViewById<AppCompatTextView>(R.id.text_value).text = "R$ ${finishList.sumByDouble { it.totalValue!! }}"
        holder.itemView.findViewById<RecyclerView>(R.id.recycler_order).apply {
            adapter = OrdersAdapter(finishList, ::onClickOrders)
        }
    }

    private fun onClickOrders(orderModel: OrderModel) {

    }

    override fun getItemCount(): Int = name.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position
}