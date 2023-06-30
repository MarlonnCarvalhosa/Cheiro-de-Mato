package com.marlonncarvalhosa.cheirodemato.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentHomeBinding
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import com.tsuryo.swipeablerv.SwipeLeftRightCallback
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val viewModel: HomeViewModel by viewModel()
    private var auth: FirebaseAuth? = null

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
        initListOrder()
        initListProduct()
    }

    private fun onClick() {
        binding?.includeAdmin?.imageProfile?.setOnClickListener {
            auth?.signOut()
        }
    }

    private fun initListOrder() {
        binding?.includeAdmin?.recyclerLastOrder?.apply {
            val mAdapter = OrdersAdapter(viewModel.listOrder().take(3))
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