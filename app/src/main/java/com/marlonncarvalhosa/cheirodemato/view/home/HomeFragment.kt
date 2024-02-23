package com.marlonncarvalhosa.cheirodemato.view.home

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentHomeBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.OrderDialog
import com.marlonncarvalhosa.cheirodemato.util.OrderDialogCommand
import com.marlonncarvalhosa.cheirodemato.util.showSnackbarRed
import com.marlonncarvalhosa.cheirodemato.util.toFormattedDate
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewState
import com.marlonncarvalhosa.cheirodemato.view.products.ProductAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModel()
    private val listOrders = mutableListOf<OrderModel>()
    private val listOProducts = mutableListOf<ProductModel>()
    private var orderDialog: OrderDialog? = null
    private val calendar = Calendar.getInstance(TimeZone.getDefault())
    private val dateModel = calendar.toFormattedDate()
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
        orderDialog = OrderDialog(requireContext())
        auth = Firebase.auth
        viewModel.getAllOrders()
        observerOrders()
        getAllProducts()
        orderDialogCommand()
        onClick()
    }

    private fun observerOrders() {
        viewModel.orderViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is OrderViewState.Loading -> {}
                is OrderViewState.SuccessGetAllOrders -> {
                    listOrders.clear()
                    listOrders.addAll(viewState.orders)
                    initListOrder(listOrders)
                    setupView()
                }
                is OrderViewState.SuccessNewOrder -> {
                    viewModel.getAllOrders()
                    orderDialog?.dismiss()
                }
                is OrderViewState.Error -> {
                    binding?.root?.showSnackbarRed(viewState.errorMessage)
                }
            }
        }
    }

    private fun orderDialogCommand() {
        orderDialog?.orderCommand?.observe(viewLifecycleOwner) { viewCommand ->
            when(viewCommand) {
                is  OrderDialogCommand.ValidationFieldsCommand -> {
                    newOrder(viewCommand.productModel)
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
            orderDialog?.show(listOProducts)
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
        binding?.includeAdmin?.textMonth?.text = dateModel.monthName?.capitalize()
        if (hide) {
            binding?.includeAdmin?.btnHide?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.show))
            binding?.includeAdmin?.textValue?.text = "R$ ----"
        } else {
            binding?.includeAdmin?.btnHide?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hide))
            val totalValue = listOrders.filter { f -> f.monthName == dateModel.monthName }
            binding?.includeAdmin?.textValue?.text = "R$ ${String.format("%.2f", totalValue.sumByDouble { it.totalValue!! })}"
        }
    }

    private fun newOrder(productModel: ProductModel) {
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
                    //dialog.dismiss()
                }
                .addOnFailureListener { exception ->
                    //dialog.dismiss()
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
                    day = dateModel.day,
                    month = dateModel.month,
                    monthName = dateModel.monthName,
                    year = dateModel.year
                )
            viewModel.newOrder((listOrders.size+1).toString(), order)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}