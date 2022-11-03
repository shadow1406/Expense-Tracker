package com.prathmesh.moneyx.ui.auth.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.prathmesh.moneyx.R
import com.prathmesh.moneyx.comman.Constants
import com.prathmesh.moneyx.comman.Resource
import com.prathmesh.moneyx.databinding.FragmentSignUpBinding
import com.prathmesh.moneyx.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up){

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        binding.apply {

            toolbar.btnBack.setOnClickListener { findNavController().popBackStack() }
            toolbar.tvTitle.text = "Sign Up"

            btnSignUp.setOnClickListener {
                if (validate()){
                    viewModel.signup(etName.text.toString(),etEmail.text.toString(),etPassword.text.toString())
                }
            }

            tvLogin.setOnClickListener {
                findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
            }

            viewModel.authState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                        progressBar.isVisible = false
                        findNavController().navigate(
                            SignUpFragmentDirections.actionSignUpFragmentToPasswordFragment(
                                isPinCreation = true
                            )
                        )
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
            }
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
        }else if (binding.etName.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Please Enter name", Toast.LENGTH_SHORT).show()
            false
        }else true
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}