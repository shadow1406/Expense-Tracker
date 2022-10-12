package com.omkarcodes.moneyx.ui.auth.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.databinding.FragmentPasswordBinding
import com.omkarcodes.moneyx.ui.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordFragment : Fragment(R.layout.fragment_password){

    private var _binding: FragmentPasswordBinding? = null
    private val binding: FragmentPasswordBinding
        get() = _binding!!
    private var password = ""
    private var confirmPassword = ""
    private val args: PasswordFragmentArgs by navArgs()
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPasswordBinding.bind(view)

        binding.apply {

            if (!args.isPinCreation){
                textView.text = "Enter your pin to continue"
            }

            tv0.setOnClickListener { numKeyPressed("0") }
            tv1.setOnClickListener { numKeyPressed("1") }
            tv2.setOnClickListener { numKeyPressed("2") }
            tv3.setOnClickListener { numKeyPressed("3") }
            tv4.setOnClickListener { numKeyPressed("4") }
            tv5.setOnClickListener { numKeyPressed("5") }
            tv6.setOnClickListener { numKeyPressed("6") }
            tv7.setOnClickListener { numKeyPressed("7") }
            tv8.setOnClickListener { numKeyPressed("8") }
            tv9.setOnClickListener { numKeyPressed("9") }

            btnBackSpace.setOnClickListener {
                if (password.isNotEmpty()){
                    password = password.substring(0,password.length-1)
                    fillCircles(password.length)
                }
            }

        }

    }

    private fun numKeyPressed(key: String){
        if (password.length < 3){
            password += key
            fillCircles(password.length)
        }else if (password.length <= 4){
            password += key
            fillCircles(password.length)
            if (args.isPinCreation){
                if (password.length == 4 && confirmPassword.isEmpty()){
                    binding.textView.text = "Re type your PIN again"
                    confirmPassword = password
                    password = ""
                    fillCircles(0)
                }else{
                    if (confirmPassword == password){
                        viewModel.savePin(confirmPassword)
                        findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToSuccessFragment())
                    }else{
                        resetPin()
                    }
                }
            }else{
                if (viewModel.checkPin(password))
                    findNavController().navigate(PasswordFragmentDirections.actionPasswordFragmentToHomeFragment())
                else
                    resetPin()
            }
        }
    }

    private fun resetPin(){
        binding.apply {
            Toast.makeText(requireContext(), "PIN does not match", Toast.LENGTH_SHORT).show()
            if (args.isPinCreation) binding.textView.text = "Let's setup your PIN"
            confirmPassword = ""
            password = ""
            fillCircles(0)
        }
    }

    private fun fillCircles(n: Int){
        val list = listOf<ImageView>(binding.pin1,binding.pin2,binding.pin3,binding.pin4)
        list.forEach { iv ->
            iv.setImageResource(R.drawable.ic_pin_empty)
        }
        for(i in 0 until n){
            list[i].setImageResource(R.drawable.ic_pin_filled)
        }
    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}