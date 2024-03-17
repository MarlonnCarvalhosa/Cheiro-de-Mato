package com.marlonncarvalhosa.cheirodemato.view.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.ItemProductBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.calculatePercentageStock
import com.marlonncarvalhosa.cheirodemato.util.formatAsCurrency
import com.marlonncarvalhosa.cheirodemato.util.toKilograms

class ProductAdapter(
    private val data: List<ProductModel>,
    private val onClickListener: (ProductModel) -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductModel) {
            item.let {
                binding.textProductName.text = it.name
                binding.textPrice.text = it.price?.formatAsCurrency()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        val amount = data[position].amount
        val amountInitStock = data[position].amountInitStock
        val color = calculatePercentageStock(amount, amountInitStock)
        holder.itemView.findViewById<View>(R.id.view2).backgroundTintList = ContextCompat.getColorStateList(holder.itemView.context, color)
        holder.itemView.findViewById<MaterialCardView>(R.id.card).setOnClickListener { onClickListener(data[position]) }
        if (data[position].type == Constants.WEIGHT) {
            holder.itemView.findViewById<AppCompatTextView>(R.id.text_amount).text = "${amountInitStock?.toKilograms()} Kg"
            holder.itemView.findViewById<AppCompatTextView>(R.id.text_layout_percentage).text = "Estoque atual: ${amount?.toKilograms()} Kg"
        } else {
            holder.itemView.findViewById<AppCompatTextView>(R.id.text_amount).text = "$amountInitStock Unidades"
            holder.itemView.findViewById<AppCompatTextView>(R.id.text_layout_percentage).text = "Estoque atual: $amount Unidades"
        }
    }

    override fun getItemCount(): Int = data.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int = position
}