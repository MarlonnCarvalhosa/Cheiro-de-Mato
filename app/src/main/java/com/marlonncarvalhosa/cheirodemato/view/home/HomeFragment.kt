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
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentHomeBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.OrderDialog
import com.marlonncarvalhosa.cheirodemato.util.OrderDialogCommand
import com.marlonncarvalhosa.cheirodemato.util.showSnackbarRed
import com.marlonncarvalhosa.cheirodemato.util.toFormattedDate
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewModel
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewState
import com.marlonncarvalhosa.cheirodemato.view.products.ProductAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.TimeZone


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val orderViewModel: OrderViewModel by viewModel()
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
        orderViewModel.getAllOrders()
        observerOrders()
        observerOrderDialogCommand()
        onClick()
        getAllProducts()
    }

    private fun observerOrders() {
        orderViewModel.orderViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is OrderViewState.Loading -> {}
                is OrderViewState.SuccessGetAllOrders -> {
                    listOrders.clear()
                    listOrders.addAll(viewState.orders)
                    initListOrder(listOrders)
                    setupView()
                }
                is OrderViewState.SuccessNewOrder -> {
                    orderViewModel.getAllOrders()
                    orderDialog?.dismiss()
                }
                is OrderViewState.SuccessUpdateOrder -> {
                    orderViewModel.getAllOrders()
                    orderDialog?.dismiss()
                }
                is OrderViewState.Error -> {
                    binding?.root?.showSnackbarRed(viewState.errorMessage)
                }
            }
        }
    }

    private fun observerOrderDialogCommand() {
        orderDialog?.orderCommand?.observe(viewLifecycleOwner) { viewCommand ->
            when(viewCommand) {
                is  OrderDialogCommand.ValidationFieldsCommand -> {
                    processOrder(viewCommand.productModel)
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
        val includeAdmin = binding?.includeAdmin

        binding?.includeAdmin?.textMonth?.text = dateModel.monthName?.capitalize()

        if (hide) {
            setHideState()
        } else {
            setShowState()
            displayTotalValue()
        }
    }

    private fun setHideState() {
        binding?.includeAdmin?.apply {
            btnHide.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.show))
            textValue.text = "R$ ----"
        }
    }

    private fun setShowState() {
        binding?.includeAdmin?.btnHide?.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.hide))
    }

    private fun displayTotalValue() {
        binding?.includeAdmin?.textValue?.text = calculateTotalValue().let { "R$ ${String.format("%.2f", it)}" }
    }

    private fun calculateTotalValue(): Double {
        return listOrders
            .filter { it.monthName == dateModel.monthName }
            .sumByDouble { it.totalValue ?: 0.0 }
    }

    private fun processOrder(productModel: ProductModel) {
        val openOrder = listOrders.firstOrNull { it.status == Constants.STATUS_WAITING }

        if (openOrder != null) {
            updateExistingOrder(openOrder, productModel)
        } else {
            createNewOrder(productModel)
        }
    }

    private fun updateExistingOrder(order: OrderModel, productModel: ProductModel) {
        val updatedItems = order.items.orEmpty().toMutableList().apply { add(productModel) }
        val totalValue = updatedItems.sumByDouble { it.price ?: 0.0 }

        val orderUpdate = hashMapOf(
            Constants.ITEMS to updatedItems,
            Constants.TOTAL_VALUE to totalValue
        )

        orderViewModel.updateOrder(order.id.toString(), orderUpdate)
    }

    private fun createNewOrder(productModel: ProductModel) {
        val newProduct = mutableListOf(productModel)
        val newOrderId = listOrders.size + 1

        val newOrder = OrderModel(
            id = newOrderId,
            status = Constants.STATUS_WAITING,
            note = "",
            totalValue = productModel.price ?: 0.0,
            items = newProduct,
            day = dateModel.day,
            month = dateModel.month,
            monthName = dateModel.monthName,
            year = dateModel.year
        )

        orderViewModel.newOrder(newOrderId.toString(), newOrder)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}