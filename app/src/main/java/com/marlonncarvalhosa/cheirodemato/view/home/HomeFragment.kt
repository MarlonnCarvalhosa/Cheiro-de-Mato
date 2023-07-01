package com.marlonncarvalhosa.cheirodemato.view.home

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentHomeBinding
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModel()
    private val mAdapter = OrdersAdapter(emptyList())
    private val listOrders = mutableListOf<OrderModel>()
    private var auth: FirebaseAuth? = null
    private val db = Firebase.firestore

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
        onClick()
        initListProduct()
        getAll()
    }

    private fun getAll() {
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                listOrders.clear()
                listOrders.addAll(result.toObjects(OrderModel::class.java))
                initListOrder(listOrders)
                var totalValue: Double = 0.0
                listOrders.forEach {it1 ->
                    totalValue += it1.totalValue!!
                }
                binding?.includeAdmin?.textValue?.text = "R$ $totalValue"
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

    private fun onClick() {
        binding?.includeAdmin?.textSeeMoreProducts?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProductsFragment())
        }
        binding?.includeAdmin?.imageProfile?.setOnClickListener {
            auth?.signOut()
        }
        binding?.btnNewOrder?.setOnClickListener {
            val order = OrderModel(
                id = listOrders.size+1,
                status = "Aguardando",
                note = "teste",
                totalValue = 35.00,
                amountProduct = viewModel.listProduct().size,
                items = viewModel.listProduct(),
                dia = "01",
                mes = "07",
                ano = "23"
            )
            val listFilter = listOrders.firstOrNull { f -> f.status == "Aguardando" }
            if (listFilter != null) {
                val orderUpdate = hashMapOf(
                    "items" to viewModel.listProduct()
                )
                db.collection(Constants.ORDERS)
                    .document(listFilter.id.toString())
                    .update(orderUpdate as Map<String, Any>)
                    .addOnSuccessListener { result ->
                        getAll()
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                    }
            } else {
                db.collection(Constants.ORDERS)
                    .document((listOrders.size+1).toString())
                    .set(order)
                    .addOnSuccessListener { result ->
                        getAll()
                    }
                    .addOnFailureListener { exception ->
                        Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                    }
            }
        }
    }

    private fun initListOrder(listOrders: MutableList<OrderModel>) {
        binding?.includeAdmin?.recyclerLastOrder?.apply {
            adapter = OrdersAdapter(listOrders.asReversed())
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

    private fun initListProduct() {
        binding?.includeAdmin?.recyclerProducts?.apply {
            val mAdapter = ProductAdapter(viewModel.listProduct().take(3))
            adapter = mAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}