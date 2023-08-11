package com.marlonncarvalhosa.cheirodemato.view.login

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.data.model.LoginModel
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentLoginBinding
import com.marlonncarvalhosa.cheirodemato.util.*
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import com.marlonncarvalhosa.cheirodemato.view.init.InitActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val viewModel: AuthViewModel by viewModel()
    private var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLoginBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as InitActivity).setColorStatusBar(R.color.white)
        auth = Firebase.auth
        onClick()
    }

    private fun onClick() {
        binding?.btnLogin?.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        val email = binding?.editEmail?.text.toString()
        val password = binding?.editPassword?.text.toString()

        if (!email.isValidEmail()){
            binding?.editEmail?.error = binding?.root?.context?.getString(R.string.error_invalid_email)
        }else{
            binding?.editEmail?.error = null
        }

        if (password.isEmpty()){
            binding?.editPassword?.error = binding?.root?.context?.getString(R.string.error_empty_password)
        }else{
            binding?.editPassword?.error = null
        }

        if (binding?.editEmail?.error.isNullOrEmpty() && binding?.editPassword?.error.isNullOrEmpty()) {
            viewModel.signInWithEmailAndPassword(email, password)
            observeAuthViewState()
        }
    }

    private fun observeAuthViewState() {
        viewModel.authViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                AuthViewState.Loading -> {
                    showProgress()
                }
                AuthViewState.Success -> {
                    activity?.openActivity<MainActivity>()
                    hideProgress()
                }
                is AuthViewState.Error -> {
                    binding?.root?.showSnackbarRed(viewState.errorMessage)
                    hideProgress()
                }
            }
        }
    }

    private fun showProgress() {
        binding?.progressBar?.viewVisible()
        binding?.btnLogin?.text = ""
    }

    private fun hideProgress() {
        binding?.progressBar?.viewInvisible()
        binding?.btnLogin?.text = "Entrar"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}