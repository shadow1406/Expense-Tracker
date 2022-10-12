package com.omkarcodes.moneyx.ui.home.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.comman.Constants
import com.omkarcodes.moneyx.comman.Resource
import com.omkarcodes.moneyx.databinding.FragmentDetailBinding
import com.omkarcodes.moneyx.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail){

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)

        binding.apply {
            args.transaction?.let { t ->

                if (t.type == "expense"){
                    toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.expenseRed))
                    clBackground.background = ContextCompat.getDrawable(requireContext(),R.drawable.bg_expense_detail)
                    btnDelete.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.expenseRed))
                    tvCategory.text = Constants.expenseCategory.find { e -> e.id == t.categoryId }?.title
                }else{
                    tvCategory.text = Constants.incomeCategory.find { i -> i.id == t.categoryId }?.title
                }
                tvType.text = t.type.capitalize()
                tvDate.text = "${t.date}  ${t.time}"
                tvDesc.text = t.description
                tvAmount.text = "₹ ${t.amount}"

                btnDelete.setOnClickListener {
                    viewModel.deleteTransaction(t)
                }
                btnBack.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.deleteTransaction.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    viewModel.getTransactions()
                    Toast.makeText(requireContext(), it.data, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    viewModel.clearDeletedTransaction()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                    viewModel.clearDeletedTransaction()
                }
            }
        }

    }

     override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}