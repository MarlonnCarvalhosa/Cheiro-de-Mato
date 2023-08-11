package com.marlonncarvalhosa.cheirodemato.view.order

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentOrderBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants

class OrderFragment : Fragment() {

    private var binding: FragmentOrderBinding? = null
    private val db = Firebase.firestore
    private val listOrders = mutableListOf<OrderModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentOrderBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        getAll()
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getAll() {
        db.collection(Constants.ORDERS)
            .get()
            .addOnSuccessListener { result ->
                listOrders.clear()
                listOrders.addAll(result.toObjects(OrderModel::class.java))
                val list = mutableListOf<String>()
                listOrders.forEach {  f ->
                    f.monthName?.let { list.add(it) }
                }
                initListOrdersByMonth(list, listOrders)
                setupLayout(listOrders)
                Log.d("ORDERRR", Gson().toJson(list.distinct().asReversed()))
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun setupLayout(list: MutableList<OrderModel>) {
        binding?.textValue?.text = "R$ ${String.format("%.2f", list.sumByDouble { it.totalValue!! })}"
    }

    private fun initListOrdersByMonth(
        list: MutableList<String>,
        listOrders: MutableList<OrderModel>
    ) {
        binding?.recyclerMonth?.apply {
            adapter = MonthAdapter(list.distinct().asReversed(), listOrders.asReversed())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}