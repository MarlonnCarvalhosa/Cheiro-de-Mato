package com.marlonncarvalhosa.cheirodemato.view.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.ItemMonthBinding
import com.marlonncarvalhosa.cheirodemato.databinding.ItemProductCartBinding
import com.marlonncarvalhosa.cheirodemato.view.home.OrdersAdapter

class ItemsCartAdapter(
    private val data: List<ProductModel>,
    private val order: OrderModel?,
    private val onClickListener: (ProductModel) -> Unit
) : RecyclerView.Adapter<ItemsCartAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductModel) {
            item.let {
                if (order?.status == "Finalizado") binding.btnTrash.visibility = View.GONE
                binding.textProductName.text = it.name
                binding.textAmount.text = it.amount.toString()
                binding.textPrice.text = "R$ ${it.price}"
                binding.btnTrash.setOnClickListener { onClickListener(item) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsCartAdapter.ViewHolder {
        val binding = ItemProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemsCartAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int = data.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position
}