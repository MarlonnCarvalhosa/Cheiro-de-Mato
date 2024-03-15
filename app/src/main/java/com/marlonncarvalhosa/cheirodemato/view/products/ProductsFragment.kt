package com.marlonncarvalhosa.cheirodemato.view.products

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentProductsBinding
import com.marlonncarvalhosa.cheirodemato.util.ProductDialogCommand
import com.marlonncarvalhosa.cheirodemato.util.removeAccents
import com.marlonncarvalhosa.cheirodemato.util.showSnackbarRed
import com.marlonncarvalhosa.cheirodemato.view.home.HomeFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsFragment : Fragment() {

    private var binding: FragmentProductsBinding? = null
    private val viewModel: ProductViewModel by viewModel()
    private val listProduct = mutableListOf<ProductModel>()
    private val productDialog: ProductDialog by lazy {
        ProductDialog(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentProductsBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts()
        observerProduct()
        observerProductDialogCommand()
        onClick()
        initListProducts(listProduct)
    }

    private fun onClick() {
        binding?.btnBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.fab?.setOnClickListener {
            productDialog.setupDialog(listProduct)
            productDialog.show()
        }
        binding?.editSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val listFilter = listProduct.filter { f ->
                    f.name!!.removeAccents().contains(s.toString().removeAccents(), true)
                }
                initListProducts(listFilter as MutableList<ProductModel>)
            }
            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun observerProduct() {
        viewModel.productViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is ProductViewState.Loading -> {}
                is ProductViewState.SuccessGetProducts -> {
                    listProduct.clear()
                    listProduct.addAll(viewState.Products)
                    initListProducts(listProduct)
                }
                is ProductViewState.SuccessNewProduct -> {
                    viewModel.getProducts()
                    productDialog.clearTextFields()
                }
                is ProductViewState.SuccessUpdateProduct -> {}
                is ProductViewState.SuccessDeleteProduct -> {}
                is ProductViewState.Error -> {
                    binding?.root?.showSnackbarRed(viewState.errorMessage)
                }
            }
        }
    }

    private fun observerProductDialogCommand() {
        productDialog.productCommand.observe(viewLifecycleOwner) { viewCommand ->
            when(viewCommand) {
                is  ProductDialogCommand.ValidationFieldsCommand -> {
                    newProduct(viewCommand.productModel)
                }
            }
        }
    }

    private fun initListProducts(listProduct: MutableList<ProductModel>) {
        binding?.recyclerProducts?.apply {
            adapter = ProductAdapter(listProduct.asReversed(), ::onClickProduct)
        }
    }

    private fun onClickProduct(productModel: ProductModel) {
        findNavController().navigate(ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment(productModel))
    }

    private fun newProduct(productModel: ProductModel) {
        val newOrderId = listProduct.size + 1
        viewModel.newProduct(newOrderId.toString(), productModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}