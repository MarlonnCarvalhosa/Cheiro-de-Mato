package com.marlonncarvalhosa.cheirodemato.view.splash

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentSplashScreenBinding
import com.marlonncarvalhosa.cheirodemato.util.openActivity
import com.marlonncarvalhosa.cheirodemato.view.init.InitActivity
import com.marlonncarvalhosa.cheirodemato.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {

    private var binding: FragmentSplashScreenBinding? = null
    private var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSplashScreenBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as InitActivity).setColorStatusBar(R.color.light_yellow)
        auth = Firebase.auth
        initDelay()
    }

    private fun initDelay() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            val currentUser = auth?.currentUser
            if (currentUser != null) {
                activity?.openActivity<MainActivity>()
            } else {
                findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}