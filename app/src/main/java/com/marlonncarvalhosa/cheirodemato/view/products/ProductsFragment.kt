package com.marlonncarvalhosa.cheirodemato.view.products

import android.app.Dialog
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.DialogNewProductBinding
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentProductsBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.view.home.ProductAdapter
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {

    private var binding: FragmentProductsBinding? = null
    private val mAdapter = ProductAdapter(emptyList())
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
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.fab?.setOnClickListener {
            initDialog()
        }
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
            setListener(object : SwipeLeftRightCallback.Listener {
                override fun onSwipedLeft(position: Int) {
                    mAdapter.notifyDataSetChanged()
                }

                override fun onSwipedRight(position: Int) {
                    mAdapter.notifyDataSetChanged()
                }
            })
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
                price = price.toString().toDouble(),
                dia = "02",
                mes = "07",
                ano = "23"
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}