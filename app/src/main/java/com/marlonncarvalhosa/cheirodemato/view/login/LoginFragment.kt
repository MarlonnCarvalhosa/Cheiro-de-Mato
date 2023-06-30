package com.marlonncarvalhosa.cheirodemato.view.login

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
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

        if (binding?.editEmail?.error.isNullOrEmpty() && binding?.editPassword?.error.isNullOrEmpty()){
            login(LoginModel(email, password))
            showProgress()
        }
    }

    private fun login(model: LoginModel) {
        auth?.signInWithEmailAndPassword(model.email, model.password)
            ?.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth?.currentUser
                    activity?.openActivity<MainActivity>()
                    hideProgress()
                } else {
                    binding?.root?.showSnackbarRed(task.exception.toString())
                    hideProgress()
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