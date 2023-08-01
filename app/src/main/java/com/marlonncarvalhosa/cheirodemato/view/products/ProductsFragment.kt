package com.marlonncarvalhosa.cheirodemato.view.products

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.DateModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.DialogNewProductBinding
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentProductsBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.MoneyTextWatcher
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class ProductsFragment : Fragment() {

    private var binding: FragmentProductsBinding? = null
    private val viewModel: ProductViewModel by viewModel()
    private val listProduct = mutableListOf<ProductModel>()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProductsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        initListProducts(listProduct)
        getAll()
        getDate()
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.fab?.setOnClickListener {
            initDialog()
        }
        binding?.editSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val listFilter = listProduct.filter {  f -> f.name!!.contains(s, true) }
                initListProducts(listFilter as MutableList<ProductModel>)
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun getAll() {
        db.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { result ->
                listProduct.clear()
                listProduct.addAll(result.toObjects(ProductModel::class.java))
                initListProducts(listProduct)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun initListProducts(listProduct: MutableList<ProductModel>) {
        binding?.recyclerProducts?.apply {
            adapter = ProductAdapter(listProduct.asReversed())
        }
    }

    private fun initSpinnerNewProductType(findViewById: AutoCompleteTextView) {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            listOf("Peso", "Unidade")
        )
        findViewById.setAdapter(adapter)
    }

    private fun initDialog() {
        val dialogBinding = DialogNewProductBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initSpinnerNewProductType(dialogBinding.editType)
        dialogBinding.editPrice.addTextChangedListener(
            MoneyTextWatcher(
                dialogBinding.editPrice,
                Locale("pt", "BR")
            )
        )
        dialog.show()
        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.editName.text
            val type = dialogBinding.editType.text
            val price = dialogBinding.editPrice.text
            val amount = dialogBinding.editAmount.text
            validation(dialog, dialogBinding, ProductModel(
                listProduct.size+1,
                name = name.toString(),
                type = type.toString(),
                amount = amount.toString().toInt(),
                price = price.toString().replace("R$", "")?.replace(".", "")?.replace(",", ".")?.filterNot { it.isWhitespace() }!!.toDouble(),
                dia = getDate().day,
                mes = getDate().month,
                ano = getDate().year
            ))
        }
    }

    private fun validation(
        dialog: Dialog,
        dialogBinding: DialogNewProductBinding,
        productModel: ProductModel
    ) {
        if (productModel.name?.isEmpty() == true){
            dialogBinding.inputNameLayout.error = binding?.root?.context?.getString(R.string.error_empty_name)
        }else{
            dialogBinding.inputNameLayout.error = null
        }
        if (productModel.type?.isEmpty() == true){
            dialogBinding.inputTypeLayout.error = binding?.root?.context?.getString(R.string.error_empty_type)
        }else{
            dialogBinding.inputTypeLayout.error = null
        }
        if (productModel.price?.toString()?.isEmpty() == true){
            dialogBinding.inputPriceLayout.error = binding?.root?.context?.getString(R.string.error_empty_price)
        }else{
            dialogBinding.inputPriceLayout.error = null
        }
        if (productModel.amount?.toString()?.isEmpty() == true){
            dialogBinding.inputAmountLayout.error = binding?.root?.context?.getString(R.string.error_empty_amount)
        }else{
            dialogBinding.inputAmountLayout.error = null
        }

        if (dialogBinding.inputNameLayout.error.isNullOrEmpty() &&  dialogBinding.inputTypeLayout.error.isNullOrEmpty() && dialogBinding.inputPriceLayout.error.isNullOrEmpty() && dialogBinding.inputAmountLayout.error.isNullOrEmpty() ) {
            newProduct(dialog, productModel)
        }
    }

    private fun newProduct(dialog: Dialog, productModel: ProductModel) {
        db.collection(Constants.PRODUCTS)
            .document((listProduct.size+1).toString())
            .set(productModel)
            .addOnSuccessListener { result ->
                getAll()
                dialog.dismiss()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun getDate(): DateModel {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val month_date = SimpleDateFormat("MMMM")
        val year = calendar.get(Calendar.YEAR)
        val monthInt: Int = calendar.get(Calendar.MONTH) + 1
        var monthConverted = "" + monthInt
        if (monthInt < 10) {
            monthConverted = "0$monthConverted"
        }

        var dayInt = calendar.get(Calendar.DAY_OF_MONTH)
        var dayConverted = "" + dayInt
        if (dayInt < 10) {
            dayConverted = "0$dayConverted"
        }
        val month_name = month_date.format(calendar.time)
        return DateModel(dayConverted, monthConverted, year.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}