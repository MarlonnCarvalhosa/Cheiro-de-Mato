package com.marlonncarvalhosa.cheirodemato.view.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentOrderBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.formatAsCurrency
import com.marlonncarvalhosa.cheirodemato.util.showSnackbarRed
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderFragment : Fragment() {

    private var binding: FragmentOrderBinding? = null
    private val viewModel: OrderViewModel by viewModel()
    private val listOrders = mutableListOf<OrderModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentOrderBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllOrders()
        observerOrders()
        onClick()
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observerOrders() {
        viewModel.orderViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is OrderViewState.Loading -> {}
                is OrderViewState.SuccessGetAllOrders -> {
                    listOrders.clear()
                    listOrders.addAll(viewState.orders)
                    val list = mutableListOf<String>()
                    listOrders.forEach {  f ->
                        f.monthName?.let { list.add(it) }
                    }
                    initListOrdersByMonth(list, listOrders)
                    setupLayout(listOrders)
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

    private fun setupLayout(list: MutableList<OrderModel>) {
        binding?.textValue?.text = list
            .filter { it.status == Constants.STATUS_FINISH }
            .sumByDouble { it.totalValue }.formatAsCurrency()
    }

    private fun initListOrdersByMonth(
        list: MutableList<String>,
        listOrders: MutableList<OrderModel>
    ) {
        binding?.recyclerMonth?.apply {
            adapter = MonthAdapter(list.distinct().asReversed(), listOrders.sortedByDescending { it.id }, ::onClickOrder)
        }
    }

    private fun onClickOrder(orderModel: OrderModel) {
        findNavController().navigate(OrderFragmentDirections.actionOrderFragmentToOrderDetailFragment(orderModel))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}