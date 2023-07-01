package com.marlonncarvalhosa.cheirodemato.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.databinding.ItemOrderBinding

class OrdersAdapter(
    private val data: List<OrderModel>,
    //private val onClickListener: (OrderModel) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderModel) {
            item.let {
                binding.textId.text = "# ${it.id}"
                binding.textStatus.text = it.status
                binding.textDate.text = "${it.dia}/${it.mes}/${it.ano}"
                binding.textValue.text = it.totalValue.toString()
                binding.textDescription.text = it.note
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersAdapter.ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrdersAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
        if (data[position].status == "Aguardando") {
            holder.itemView.findViewById<View>(R.id.view2).backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, R.color.status_yellow)
        }
    }

    override fun getItemCount(): Int = data.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position
}