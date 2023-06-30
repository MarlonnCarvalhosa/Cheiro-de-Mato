package com.marlonncarvalhosa.cheirodemato.view.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentSplashScreenBinding
import com.marlonncarvalhosa.cheirodemato.view.init.InitActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment : Fragment() {

    private var binding: FragmentSplashScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSplashScreenBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as InitActivity).setColorStatusBar(R.color.light_yellow)
        initDelay()
    }

    private fun initDelay() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}