package com.marlonncarvalhosa.cheirodemato.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentLoginBinding
import com.marlonncarvalhosa.cheirodemato.util.*
import com.marlonncarvalhosa.cheirodemato.view.init.InitActivity
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLoginBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as InitActivity).setColorStatusBar(R.color.white)
        Firebase.auth
        onClick()
        observeAuthViewState()
    }

    private fun onClick() {
        binding?.btnLogin?.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        val email = binding?.editEmail?.text.toString()
        val password = binding?.editPassword?.text.toString()

        binding?.editEmail?.error = if (!email.isValidEmail()) getString(R.string.error_invalid_email) else null
        binding?.editPassword?.error = if (password.isEmpty()) getString(R.string.error_empty_password) else null

        if (binding?.editEmail?.error.isNullOrEmpty() && binding?.editPassword?.error.isNullOrEmpty()) {
            viewModel.signInWithEmailAndPassword(email, password)
        }
    }

    private fun observeAuthViewState() {
        viewModel.authViewState.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is AuthViewState.Loading -> showProgress()
                is AuthViewState.Success -> {
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
        binding?.btnLogin?.text = getString(R.string.text_login_button)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}