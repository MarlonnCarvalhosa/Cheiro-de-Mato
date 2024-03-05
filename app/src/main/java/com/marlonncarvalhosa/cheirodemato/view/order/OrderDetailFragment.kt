package com.marlonncarvalhosa.cheirodemato.view.order

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentOrderDetailBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.MoneyTextWatcher
import com.marlonncarvalhosa.cheirodemato.util.formatAsCurrency
import com.marlonncarvalhosa.cheirodemato.util.hideKeyBoard
import com.marlonncarvalhosa.cheirodemato.util.showSnackbarRed
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class OrderDetailFragment : Fragment() {

    private var binding: FragmentOrderDetailBinding? = null
    private val orderViewModel: OrderViewModel by viewModel()
    private val listProduct = mutableListOf<ProductModel>()
    private val args: OrderDetailFragmentArgs by navArgs()
    private var order: OrderModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentOrderDetailBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderViewModel.getOrderById(args.orderModel.id.toString())
        observerOrders()
        onClick()
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.btnChange?.setOnClickListener {
            calculateChange(order)
            hideKeyBoard(it)
        }
        binding?.btnFinish?.setOnClickListener {
            finishOrder()
        }
    }

    private fun observerOrders() {
        orderViewModel.orderViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is OrderViewState.Loading -> {}
                is OrderViewState.SuccessGetAllOrders -> {}
                is OrderViewState.SuccessGetOrderById -> {
                    listProduct.clear()
                    order = viewState.order
                    order?.items?.let { listProduct.addAll(it) }
                    initListItems(listProduct, order)
                    setupView(order)
                }
                is OrderViewState.SuccessNewOrder -> {}
                is OrderViewState.SuccessUpdateOrder -> {
                    orderViewModel.getOrderById(order?.id.toString())
                }
                is OrderViewState.SuccessDeleteOrder -> {
                    findNavController().popBackStack()
                }
                is OrderViewState.Error -> {
                    binding?.root?.showSnackbarRed(viewState.errorMessage)
                }
            }
        }
    }

    private fun setupView(order: OrderModel?) {
        if (order?.status == Constants.STATUS_FINISH) {
            binding?.editChange?.visibility = View.GONE
            binding?.btnChange?.visibility = View.GONE
            binding?.textChangeValue?.visibility = View.GONE
            binding?.textChangeAll?.visibility = View.GONE
            binding?.inputLayoutChange?.visibility = View.GONE
            binding?.textLayoutChange?.visibility = View.GONE
            binding?.btnFinish?.visibility = View.GONE
        } else {
            binding?.editChange?.visibility = View.VISIBLE
            binding?.btnChange?.visibility = View.VISIBLE
            binding?.textChangeValue?.visibility = View.VISIBLE
            binding?.textChangeAll?.visibility = View.VISIBLE
            binding?.inputLayoutChange?.visibility = View.VISIBLE
            binding?.textLayoutChange?.visibility = View.VISIBLE
            binding?.btnFinish?.visibility = View.VISIBLE
        }
        binding?.textTotalValue?.text = order?.totalValue?.formatAsCurrency()
        binding?.editChange?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                binding?.btnChange?.isEnabled = s.toString() != ""
            }
        })
        binding?.editChange?.addTextChangedListener(
            MoneyTextWatcher(
                binding?.editChange,
                Locale("pt", "BR")
            )
        )
        calculateChange(order)
    }

    private fun calculateChange(actualOrder: OrderModel?) {
        val rawText = binding?.editChange?.text.toString()
        val value = rawText.replace("R$", "").replace(".", "").replace(",", ".").filterNot { it.isWhitespace() }.toDoubleOrNull()

        if (value != null && value != actualOrder?.totalValue) {
            binding?.textChangeValue?.text = actualOrder?.totalValue?.let { value.minus(it).formatAsCurrency() }
        }
    }

    private fun initListItems(listOrders: MutableList<ProductModel>, order: OrderModel?) {
        binding?.recyclerProducts?.apply {
            adapter = ItemsCartAdapter(listOrders, order, ::onClickDelete)
        }
    }

    private fun onClickDelete(model: ProductModel) {
        listProduct.removeIf {
            it.id == model.id
        }
        processOrder(model)
    }

    private fun processOrder(productModel: ProductModel) {
        if (listProduct.isNotEmpty()) {
            updateOrder(productModel)
        } else {
            orderViewModel.deleteOrder(order?.id.toString())
        }
    }

    private fun updateOrder(productModel: ProductModel) {
        val orderUpdate = hashMapOf(
            Constants.ITEMS to listProduct,
            Constants.TOTAL_VALUE to productModel.totalPrice?.let { order?.totalValue?.minus(it) }
        )
        orderViewModel.updateOrder(order?.id.toString(), orderUpdate as Map<String, Any>)
    }

    private fun finishOrder() {
        val orderUpdate = hashMapOf(
            Constants.ITEMS to listProduct,
            Constants.STATUS to Constants.STATUS_FINISH
        )
        orderViewModel.updateOrder(order?.id.toString(), orderUpdate as Map<String, Any>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}