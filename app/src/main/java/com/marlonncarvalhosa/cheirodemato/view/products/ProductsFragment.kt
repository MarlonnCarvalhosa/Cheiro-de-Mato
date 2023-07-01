package com.marlonncarvalhosa.cheirodemato.view.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentProductsBinding
import com.marlonncarvalhosa.cheirodemato.view.home.ProductAdapter
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {

    private var binding: FragmentProductsBinding? = null
    private val mAdapter = ProductAdapter(emptyList())
    private val viewModel: ProductViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProductsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick()
        initListProducts()
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.fab?.setOnClickListener {

        }
    }

    private fun initListProducts() {
        binding?.recyclerProducts?.apply {
            adapter = ProductAdapter(viewModel.listProduct().take(3))
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