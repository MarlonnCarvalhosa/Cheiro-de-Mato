package com.marlonncarvalhosa.cheirodemato.view.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.ItemProductCartBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.formatAsCurrency
import com.marlonncarvalhosa.cheirodemato.util.toKilograms

class ItemsCartAdapter(
    private val data: List<ProductModel>,
    private val order: OrderModel?,
    private val onClickListener: (ProductModel) -> Unit
) : RecyclerView.Adapter<ItemsCartAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductModel) {
            item.let {
                if (order?.status == Constants.STATUS_FINISH) binding.btnTrash.visibility = View.GONE
                binding.textProductName.text = it.name
                binding.textPrice.text = it.totalPrice?.formatAsCurrency()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsCartAdapter.ViewHolder {
        val binding = ItemProductCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemsCartAdapter.ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.findViewById<View>(R.id.btn_trash).setOnClickListener {
            onClickListener(data[position])
        }
        if (data[position].type == Constants.WEIGHT) {
            holder.itemView.findViewById<AppCompatTextView>(R.id.text_amount).text = "${data[position].amountBuy?.toKilograms()} Kg"
        } else {
            holder.itemView.findViewById<AppCompatTextView>(R.id.text_amount).text = "${data[position].amountBuy} Unidades"
        }
    }

    override fun getItemCount(): Int = data.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position
}