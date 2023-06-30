package com.marlonncarvalhosa.cheirodemato.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marlonncarvalhosa.cheirodemato.util.Constants
import com.marlonncarvalhosa.cheirodemato.util.MaskEditUtil
import com.marlonncarvalhosa.cheirodemato.util.openActivity
import com.marlonncarvalhosa.cheirodemato.R
import com.marlonncarvalhosa.cheirodemato.databinding.FragmentLoginBinding
import com.marlonncarvalhosa.cheirodemato.view.MainActivity
import com.marlonncarvalhosa.cheirodemato.view.init.InitActivity

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLoginBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as InitActivity).setColorStatusBar(R.color.white)
        onClick()
    }

    private fun onClick() {
        binding?.btnLogin?.setOnClickListener {
            activity?.openActivity<MainActivity>()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}