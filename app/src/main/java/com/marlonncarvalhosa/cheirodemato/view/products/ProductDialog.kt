package com.marlonncarvalhosa.cheirodemato.view.products

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.DialogNewOrderBinding
import com.marlonncarvalhosa.cheirodemato.databinding.DialogNewProductBinding
import com.marlonncarvalhosa.cheirodemato.util.MoneyTextWatcher
import com.marlonncarvalhosa.cheirodemato.util.ProductDialogCommand
import com.marlonncarvalhosa.cheirodemato.util.toFormattedDate
import org.koin.android.R.*
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class ProductDialog(private val context: Context) {
    private val binding = DialogNewProductBinding.inflate((context as AppCompatActivity).layoutInflater)
    private val dialog = Dialog(context)
    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private val dateModel = calendar.toFormattedDate()
    private val _productCommand = MutableLiveData<ProductDialogCommand>()
    val productCommand: LiveData<ProductDialogCommand>
        get() = _productCommand

    fun setupDialog(listProduct: List<ProductModel>) {
        dialog.setContentView(binding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initSpinnerNewProductType(binding.editType)
        binding.editPrice.addTextChangedListener(
            MoneyTextWatcher(
                binding.editPrice,
                Locale("pt", "BR")
            )
        )
        binding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnSave.setOnClickListener {
            val name = binding.editName.text
            val type = binding.editType.text
            val price = binding.editPrice.text
            val amount = binding.editAmount.text
            validation(ProductModel(
                listProduct.size + 1,
                name = name.toString(),
                type = type.toString(),
                amountInitStock = amount.toString().toInt(),
                amount = amount.toString().toInt(),
                price = price.toString().replace("R$", "").replace(".", "").replace(",", ".").filterNot { it.isWhitespace() }.toDouble(),
                dia = dateModel.day,
                mes = dateModel.month,
                ano = dateModel.year
            )
            )
        }
    }

    fun clearTextFields() {
        binding.editName.requestFocus()
        binding.editName.setText("")
        binding.editType.setText("")
        binding.editPrice.setText("R$ 0.00")
        binding.editAmount.setText("")
    }

    private fun initSpinnerNewProductType(findViewById: AutoCompleteTextView) {
        val adapter = ArrayAdapter(
            context,
            layout.support_simple_spinner_dropdown_item,
            listOf("Peso", "Unidade")
        )
        findViewById.setAdapter(adapter)
    }

    private fun validation(
        productModel: ProductModel
    ) {
        if (productModel.name?.isEmpty() == true) {
            binding.inputNameLayout.error = context.getString(R.string.error_empty_name)
        } else {
            binding.inputNameLayout.error = null
        }
        if (productModel.type?.isEmpty() == true) {
            binding.inputTypeLayout.error = context.getString(R.string.error_empty_type)
        } else {
            binding.inputTypeLayout.error = null
        }
        if (productModel.totalPrice?.toString()?.isEmpty() == true) {
            binding.inputPriceLayout.error = context.getString(R.string.error_empty_price)
        } else {
            binding.inputPriceLayout.error = null
        }
        if (productModel.amount?.toString()?.isEmpty() == true) {
            binding.inputAmountLayout.error = context.getString(R.string.error_empty_amount)
        } else {
            binding.inputAmountLayout.error = null
        }

        if (binding.inputNameLayout.error.isNullOrEmpty() && binding.inputTypeLayout.error.isNullOrEmpty() && binding.inputPriceLayout.error.isNullOrEmpty() && binding.inputAmountLayout.error.isNullOrEmpty()) {
            _productCommand.value = ProductDialogCommand.ValidationFieldsCommand(productModel)
        }
    }

    fun show() {
        dialog.show()
    }
    fun dismiss() {
        dialog.dismiss()
    }
}