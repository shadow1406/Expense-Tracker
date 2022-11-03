package com.prathmesh.moneyx.ui.auth.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.prathmesh.moneyx.R
import com.prathmesh.moneyx.databinding.FragmentSignupSuccessBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SuccessFragment : Fragment(R.layout.fragment_signup_success){

    private var _binding: FragmentSignupSuccessBinding? = null
    private val binding: FragmentSignupSuccessBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupSuccessBinding.bind(view)

        lifecycleScope.launch {
            delay(1000L)
            findNavController().navigate(SuccessFragmentDirections.actionSuccessFragmentToHomeFragment())
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}