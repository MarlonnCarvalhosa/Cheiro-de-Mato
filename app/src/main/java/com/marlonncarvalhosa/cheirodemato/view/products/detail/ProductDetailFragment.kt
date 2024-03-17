package com.marlonncarvalhosa.cheirodemato.view.products.detail

import android.graphics.ColorSpace.Model
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentOrderDetailBinding
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentProductDetailBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.calculatePercentageStock
import com.marlonncarvalhosa.cheirodemato.util.formatAsCurrency
import com.marlonncarvalhosa.cheirodemato.util.showSnackbarRed
import com.marlonncarvalhosa.cheirodemato.util.toKilograms
import com.marlonncarvalhosa.cheirodemato.view.home.HomeFragmentDirections
import com.marlonncarvalhosa.cheirodemato.view.home.OrdersAdapter
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import com.marlonncarvalhosa.cheirodemato.view.order.OrderDetailFragmentArgs
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewModel
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewState
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailFragment : Fragment() {

    private var binding: FragmentProductDetailBinding? = null
    private val orderViewModel: OrderViewModel by viewModel()
    private var productModel: ProductModel? = null
    private val args: ProductDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProductDetailBinding.inflate(layoutInflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setColorStatusBar(R.color.main_green)
        productModel = args.productModel
        orderViewModel.getAllOrders()
        observerOrders()
        setupView()
        onClick()
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observerOrders() {
        orderViewModel.orderViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is OrderViewState.Loading -> {}
                is OrderViewState.SuccessGetAllOrders -> {
                    val filteredOrders = viewState.orders.filter { order ->
                        order.items?.any { it.id == productModel?.id } ?: false
                    }
                    initListOrder(filteredOrders.toMutableList())
                    setupView()
                }

                is OrderViewState.SuccessGetOrderById -> {}
                is OrderViewState.SuccessNewOrder -> {}
                is OrderViewState.SuccessUpdateOrder -> {}
                is OrderViewState.SuccessDeleteOrder -> {}
                is OrderViewState.Error -> {
                    binding?.root?.showSnackbarRed(viewState.errorMessage)
                }
            }
        }
    }

    private fun setupView() {
        val statusStockColor = calculatePercentageStock(productModel?.amount, productModel?.amountInitStock)
        binding?.textName?.text = productModel?.name
        binding?.textPrice?.text = productModel?.price?.formatAsCurrency()
        binding?.view2?.backgroundTintList = ContextCompat.getColorStateList(requireContext(), statusStockColor)
        if (productModel?.type == Constants.WEIGHT) {
            binding?.textLayoutAmount?.text = "Estoque inicial:  ${productModel?.amountInitStock?.toKilograms()} Kg"
            binding?.textLayoutActualAmount?.text = "Estoque atual:  ${productModel?.amount?.toKilograms()} Kg"
        } else {
            binding?.textLayoutAmount?.text = "Estoque inicial: ${productModel?.amountInitStock} Unidades"
            binding?.textLayoutActualAmount?.text = "Estoque atual:  ${productModel?.amount} Unidades"
        }
    }

    private fun initListOrder(listOrders: MutableList<OrderModel>) {
        binding?.recyclerHistoric?.apply {
            adapter = OrdersAdapter(listOrders.asReversed().take(3), ::onClickOrders)
        }
    }

    private fun onClickOrders(orderModel: OrderModel) {
        findNavController().navigate(ProductDetailFragmentDirections.actionProductDetailFragmentToOrderDetailFragment(orderModel))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}