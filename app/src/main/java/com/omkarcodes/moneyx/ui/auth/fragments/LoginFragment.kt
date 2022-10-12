package com.omkarcodes.moneyx.ui.auth.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.comman.Constants
import com.omkarcodes.moneyx.comman.Resource
import com.omkarcodes.moneyx.databinding.FragmentLoginBinding
import com.omkarcodes.moneyx.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login){

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.apply {

            toolbar.btnBack.setOnClickListener { findNavController().popBackStack() }
            toolbar.tvTitle.text = "Login"

            btnLogin.setOnClickListener {
                if (validate()){
                    viewModel.login(etEmail.text.toString(),etPassword.text.toString())
                }
            }

            tvSignUp.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
            }

            viewModel.authState.observe(viewLifecycleOwner,{
                when(it){
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                        progressBar.isVisible = false
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPasswordFragment(isPinCreation = true))
                    }
                    is Resource.Loading -> {
                        progressBar.isVisible = true
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        progressBar.isVisible = false

                    }
                    else -> {}
                }
            })
        }
    }

    private fun validate() : Boolean{
        val email = binding.etEmail.text.toString()
        val pass = binding.etPassword.text.toString()
        return if (email.isEmpty() or !Constants.EMAIL_REGEX.toRegex().containsMatchIn(email)){
            Toast.makeText(requireContext(), "Enter valid email", Toast.LENGTH_SHORT).show()
            false
        }else if (pass.isEmpty() or (pass.length < 8)){
            Toast.makeText(requireContext(), "Password length should be 8 or more", Toast.LENGTH_SHORT).show()
            false
        }else true
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}