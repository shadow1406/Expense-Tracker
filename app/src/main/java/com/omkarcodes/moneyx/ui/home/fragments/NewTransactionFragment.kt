package com.omkarcodes.moneyx.ui.home.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.comman.Constants
import com.omkarcodes.moneyx.comman.Resource
import com.omkarcodes.moneyx.databinding.FragmentNewTransactionBinding
import com.omkarcodes.moneyx.ui.home.HomeViewModel
import com.omkarcodes.moneyx.ui.home.Transaction
import com.omkarcodes.moneyx.ui.home.adapters.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NewTransactionFragment : Fragment(R.layout.fragment_new_transaction){

    private var _binding: FragmentNewTransactionBinding? = null
    private val binding: FragmentNewTransactionBinding
        get() = _binding!!
    private val args: NewTransactionFragmentArgs by navArgs()
    private val viewModel: HomeViewModel by activityViewModels()
    private var categoryId: Int = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewTransactionBinding.bind(view)


        binding.apply {

            editText.requestFocus()

            if (!args.isIncome){
                val red = ContextCompat.getColor(requireContext(),R.color.expenseRed)
                mainCl.setBackgroundColor(red)
                btnContinue.setBackgroundColor(red)
                tvTitle.text = "Expense"
                spnCategory.adapter = CategoryAdapter(Constants.expenseCategory,requireContext())
            }else{
                spnCategory.adapter = CategoryAdapter(Constants.incomeCategory,requireContext())
            }

            spnCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                    categoryId = if (args.isIncome)
                        Constants.incomeCategory[i].id
                    else
                        Constants.expenseCategory[i].id
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    categoryId = if (args.isIncome)
                        Constants.incomeCategory[0].id
                    else
                        Constants.expenseCategory[0].id
                }
            }

            btnContinue.setOnClickListener {
                if (validate()){
                    val transaction = Transaction(
                        categoryId = categoryId,
                        amount = editText.text.toString(),
                        time = getTime(),
                        date = getDate(),
                        description = etDesc.text.toString(),
                        type = if (args.isIncome) "income" else "expense"
                    )
                    viewModel.addTransaction(transaction)
                }
            }
            btnBack.setOnClickListener { findNavController().popBackStack() }

            viewModel.newTransaction.observe(viewLifecycleOwner,{
                when(it){
                    is Resource.Success -> {
                        viewModel.getTransactions()
                        Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                        viewModel.clearNewTransaction()
                        findNavController().popBackStack()
                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        viewModel.clearNewTransaction()
                        findNavController().popBackStack()
                    }
                    else -> {}
                }
            })
        }
    }

    private fun validate() : Boolean {
        return if(binding.editText.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Please enter amount", Toast.LENGTH_SHORT).show()
            false
        }else if (binding.etDesc.text.toString().isEmpty()){
            Toast.makeText(requireContext(), "Please enter description", Toast.LENGTH_SHORT).show()
            false
        }else true
    }

    private fun getTime(): String {
        return SimpleDateFormat("hh:mm a",Locale.getDefault()).format(Date())
    }

    private fun getDate(): String {
        return SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(Date())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}