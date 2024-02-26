package com.marlonncarvalhosa.cheirodemato.view.order

import android.content.Context
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.DialogNewOrderBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.OrderDialogCommand
import com.marlonncarvalhosa.cheirodemato.util.toFormattedDate
import java.util.Calendar
import java.util.TimeZone

class OrderDialog(private val context: Context) {
    private val dialogBinding = DialogNewOrderBinding.inflate((context as AppCompatActivity).layoutInflater)
    private var selectedProduct: ProductModel? = null
    private var dialog: Dialog? = null
    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private val dateModel = calendar.toFormattedDate()
    private val _orderCommand = MutableLiveData<OrderDialogCommand>()
    val orderCommand: LiveData<OrderDialogCommand>
        get() = _orderCommand

    fun show(listOProducts: List<ProductModel>) {
        dialog = createDialog()

        val parentView = dialogBinding.root.parent as? ViewGroup
        parentView?.removeView(dialogBinding.root)

        dialog?.setContentView(dialogBinding.root)

        val productAdapter = ArrayAdapter(
            context,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            listOProducts.map { it.name }
        )
        dialogBinding.editName.setAdapter(productAdapter)

        setupTextWatcher(listOProducts)

        dialog?.show()

        dialogBinding.btnClose.setOnClickListener {
            dialog?.dismiss()
        }


        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.editName.text.toString()
            val price = dialogBinding.editPrice.text.toString()
            val amount = dialogBinding.editAmount.text.toString().toIntOrNull() ?: 0

            selectedProduct?.let {
                validation(ProductModel(
                    id = it.id,
                    name = name,
                    type = it.type,
                    amount = amount,
                    price = price.replace("R$", "").replace(".", "").replace(",", ".").toDoubleOrNull() ?: 0.0,
                    dia = dateModel.day,
                    mes = dateModel.month,
                    ano = dateModel.year
                ))
            }
        }
    }

    private fun createDialog(): Dialog {
        val dialog = Dialog(context)
        val parentView = dialogBinding.root.parent as? ViewGroup
        parentView?.removeView(dialogBinding.root)

        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    private fun setupTextWatcher(listOProducts: List<ProductModel>) {
        dialogBinding.editName.setOnItemClickListener { adapterView, _, i, _ ->
            val nameSelected = adapterView.getItemAtPosition(i).toString()
            selectedProduct = listOProducts.firstOrNull { f -> f.name == nameSelected }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    updateFinalPrice(s.toString())
                }
            }

            dialogBinding.editAmount.addTextChangedListener(textWatcher)
        }
    }

    private fun updateFinalPrice(amountText: String) {
        if (amountText.isNotBlank()) {
            val calculatedPrice = selectedProduct?.let {
                if (it.type == Constants.WEIGHT) {
                    it.price?.div(100)?.times(amountText.toDoubleOrNull() ?: 0.0)
                } else {
                    it.price?.times(amountText.toDoubleOrNull() ?: 0.0)
                }
            }

            calculatedPrice?.let {
                dialogBinding.editPrice.setText("R$ ${String.format("%.2f", it)}")
            } ?: run {
                dialogBinding.editPrice.setText("R$ 0.00")
            }
        } else {
            dialogBinding.editPrice.setText("R$ 0.00")
        }
    }

    fun clearTextFields() {
        dialogBinding.editName.requestFocus()
        dialogBinding.editName.setText("")
        dialogBinding.editPrice.setText("")
        dialogBinding.editAmount.setText("")
        selectedProduct = ProductModel()
    }

    private fun validation(
        productModel: ProductModel
    ) {
        if (productModel.name?.isEmpty() == true) {
            dialogBinding.inputNameLayout.error =
                context.getString(R.string.error_empty_name)
        } else {
            dialogBinding.inputNameLayout.error = null
        }

        if (productModel.price?.toString()?.isEmpty() == true) {
            dialogBinding.inputPriceLayout.error =
                context.getString(R.string.error_empty_price)
        } else {
            dialogBinding.inputPriceLayout.error = null
        }

        if (productModel.amount?.toString()?.isEmpty() == true) {
            dialogBinding.inputAmountLayout.error =
                context.getString(R.string.error_empty_amount)
        } else {
            dialogBinding.inputAmountLayout.error = null
        }

        if (dialogBinding.inputNameLayout.error.isNullOrEmpty() && dialogBinding.inputPriceLayout.error.isNullOrEmpty() && dialogBinding.inputAmountLayout.error.isNullOrEmpty() ) {
            _orderCommand.value = OrderDialogCommand.ValidationFieldsCommand(productModel)
        }
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}