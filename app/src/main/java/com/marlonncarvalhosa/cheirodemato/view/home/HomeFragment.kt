package com.marlonncarvalhosa.cheirodemato.view.home

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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.DateModel
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.DialogNewOrderBinding
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentHomeBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.MoneyTextWatcher
import com.marlonncarvalhosa.cheirodemato.util.showSnackbarRed
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewState
import com.marlonncarvalhosa.cheirodemato.view.products.ProductAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModel()
    private val listOrders = mutableListOf<OrderModel>()
    private val listOProducts = mutableListOf<ProductModel>()
    private var auth: FirebaseAuth? = null
    private val db = Firebase.firestore
    private var hide = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentHomeBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setColorStatusBar(R.color.white)
        auth = Firebase.auth
        viewModel.getAllOrders()
        observerOrders()
        getAllProducts()
        onClick()
    }

    private fun observerOrders() {
        viewModel.orderViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is OrderViewState.Loading -> {}
                is OrderViewState.SuccessGetAllOrder -> {
                    listOrders.clear()
                    listOrders.addAll(viewState.orders)
                    initListOrder(listOrders)
                    setupView()
                }
                is OrderViewState.ErrorGetAllOrder -> {
                    binding?.root?.showSnackbarRed(viewState.errorMessage)
                }
            }
        }
    }

    private fun getAllProducts() {
        db.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { result ->
                listOProducts.clear()
                listOProducts.addAll(result.toObjects(ProductModel::class.java))
                initListProduct(listOProducts)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun onClick() {
        binding?.includeAdmin?.textSeeMoreProducts?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductsFragment())
        }
        binding?.includeAdmin?.textSeeMoreOrder?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOrderFragment())
        }
        binding?.includeAdmin?.btnHide?.setOnClickListener {
            hide = !hide
            setupView()
        }
        binding?.includeAdmin?.imageProfile?.setOnClickListener {
            auth?.signOut()
        }
        binding?.btnNewOrder?.setOnClickListener {
            initDialog()
        }
    }

    private fun initListOrder(listOrders: MutableList<OrderModel>) {
        binding?.includeAdmin?.recyclerLastOrder?.apply {
            adapter = OrdersAdapter(listOrders.asReversed().take(3), ::onClickOrders)
        }
    }

    private fun onClickOrders(orderModel: OrderModel) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToOrderDetailFragment(orderModel))
    }

    private fun initListProduct(listOProducts: MutableList<ProductModel>) {
        binding?.includeAdmin?.recyclerProducts?.apply {
            val mAdapter = ProductAdapter(listOProducts.asReversed().take(3))
            adapter = mAdapter
        }
    }

    private fun setupView() {
        binding?.includeAdmin?.textMonth?.text = getDate().monthName?.capitalize()
        if (hide) {
            binding?.includeAdmin?.btnHide?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.show))
            binding?.includeAdmin?.textValue?.text = "R$ ----"
        } else {
            binding?.includeAdmin?.btnHide?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hide))
            val totalValue = listOrders.filter { f -> f.monthName == getDate().monthName }
            binding?.includeAdmin?.textValue?.text = "R$ ${String.format("%.2f", totalValue.sumByDouble { it.totalValue!! })}"
        }
    }

    private fun initDialog() {
        var finalPrice: Double? = null
        var product: ProductModel? = null
        val dialogBinding = DialogNewOrderBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            listOProducts
        )
        dialogBinding.editName.setAdapter(adapter)
        dialogBinding.editPrice.addTextChangedListener(
            MoneyTextWatcher(
                dialogBinding.editPrice,
                Locale("pt", "BR")
            )
        )
        dialogBinding.editName.setOnItemClickListener { adapterView, view, i, l ->
            val typeSelected = adapterView.getItemAtPosition(i).toString()
            product = listOProducts.firstOrNull { f -> f.name == typeSelected }
            if (product?.type == "Peso") {
                dialogBinding.editAmount.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        if (s.toString() != "") {
                            finalPrice = product?.price?.div(100)?.times(s.toString().toDouble() ?: 0.0)
                            dialogBinding.editPrice.setText("R$ ${String.format("%.2f", finalPrice)}")
                        } else {
                            dialogBinding.editPrice.setText("R$ 0.00")
                        }
                    }
                })
            } else {
                dialogBinding.editAmount.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: Editable) {
                        if (s.toString() != "") {
                            finalPrice = product?.price?.times(s.toString().toDouble())
                            dialogBinding.editPrice.setText("R$ ${String.format("%.2f", finalPrice)}")
                        } else {
                            dialogBinding.editPrice.setText("R$ 0.00")
                        }
                    }
                })
            }
        }
        dialog.show()
        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.editName.text
            val price = dialogBinding.editPrice.text
            val amount = dialogBinding.editAmount.text
            validation(dialog, dialogBinding, ProductModel(
                id = product?.id,
                name = name.toString(),
                type = product?.type,
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
        dialogBinding: DialogNewOrderBinding,
        productModel: ProductModel
    ) {
        if (productModel.name?.isEmpty() == true){
            dialogBinding.inputNameLayout.error = binding?.root?.context?.getString(R.string.error_empty_name)
        }else{
            dialogBinding.inputNameLayout.error = null
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

        if (dialogBinding.inputNameLayout.error.isNullOrEmpty() && dialogBinding.inputPriceLayout.error.isNullOrEmpty() && dialogBinding.inputAmountLayout.error.isNullOrEmpty() ) {
            newOrder(dialog, productModel)
        }
    }

    private fun newOrder(dialog: Dialog, productModel: ProductModel) {
        val openOrder = listOrders.firstOrNull { f -> f.status == "Aguardando" }
        if (openOrder != null) {
            val listToUpdate = mutableListOf<ProductModel>()
            openOrder.items?.let { listToUpdate.addAll(it) }
            listToUpdate.add(productModel)
            val orderUpdate = hashMapOf(
                "items" to listToUpdate,
                "totalValue" to listToUpdate.sumByDouble { it.price!! }
            )
            db.collection(Constants.ORDERS)
                .document(openOrder.id.toString())
                .update(orderUpdate as Map<String, Any>)
                .addOnSuccessListener { result ->
                    viewModel.getAllOrders()
                    dialog.dismiss()
                }
                .addOnFailureListener { exception ->
                    dialog.dismiss()
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }
        } else {
            val newProduct = mutableListOf<ProductModel>()
            newProduct.add(productModel)
            val order = OrderModel(
                    id = listOrders.size+1,
                    status = "Aguardando",
                    note = "",
                    totalValue = productModel.price,
                    items = newProduct,
                    day = getDate().day,
                    month = getDate().month,
                    monthName = getDate().monthName,
                    year = getDate().year
                )
            db.collection(Constants.ORDERS)
                .document((listOrders.size+1).toString())
                .set(order)
                .addOnSuccessListener { result ->
                    viewModel.getAllOrders()
                    dialog.dismiss()
                }
                .addOnFailureListener { exception ->
                    dialog.dismiss()
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }
    }

    private fun getDate(): DateModel {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        val month_date = SimpleDateFormat("MMMM")
        val year = calendar.get(Calendar.YEAR)
        var dayInt = calendar.get(Calendar.DAY_OF_MONTH)
        var dayConverted = "" + dayInt
        if (dayInt < 10) {
            dayConverted = "0$dayConverted"
        }

        val monthInt: Int = calendar.get(Calendar.MONTH) + 1
        var monthConverted = "" + monthInt
        if (monthInt < 10) {
            monthConverted = "0$monthConverted"
        }

        val month_name = month_date.format(calendar.time)

        return DateModel(dayConverted, monthConverted, year.toString(), month_name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}