package com.prathmesh.moneyx.ui.home.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prathmesh.moneyx.R
import com.prathmesh.moneyx.comman.Resource
import com.prathmesh.moneyx.comman.getMonthInWords
import com.prathmesh.moneyx.comman.toDateInMillis
import com.prathmesh.moneyx.databinding.FragmentHomeBinding
import com.prathmesh.moneyx.ui.home.HomeViewModel
import com.prathmesh.moneyx.ui.home.Transaction
import com.prathmesh.moneyx.ui.home.adapters.TransactionAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),TransactionAdapter.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        viewModel.getTransactions()

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        binding.btnMonth.text = calendar.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.getDefault())
        binding.rvRecent.layoutManager = LinearLayoutManager(requireContext())

        val currentMonth = SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(Date())

        viewModel.transactions.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.data!!.isNotEmpty())
                        setupHomeScreen(it.data, currentMonth)
                    else
                        setupNoTransaction()
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
                else -> {}
            }
        }
        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()).split("-")
        val month = date[1]
        binding.btnMonth.text = month.getMonthInWords()

        binding.btnLogout.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToLogOutBottomSheet())
        }

        binding.ivProfile.setOnClickListener{
            viewModel.drawerOpen.postValue(true)
        }
    }

    private fun setupNoTransaction() {
        binding.apply {
            tvNotFound.isVisible = true
            rvRecent.isVisible = false
            tvBalance.text = "₹ 0"
            tvIncome.text = "₹ 0"
            tvExpense.text = "₹ 0"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupHomeScreen(data: List<Transaction>?, currentMonth: String) {
        data?.let { list ->
            binding.tvNotFound.isVisible = false
            binding.rvRecent.isVisible = true
            var income = 0
            var expenses = 0
            list.forEach { t ->
                if (t.type == "income")
                    income += t.amount.toInt()
                else
                    expenses += t.amount.toInt()
            }
            val balance = income - expenses
            val filteredList = list.sortedByDescending { t -> (t.date+ " "+t.time).toDateInMillis() }

            binding.apply {
                tvBalance.text = "₹ $balance"
                tvIncome.text = "₹ $income"
                tvExpense.text = "₹ $expenses"
                rvRecent.adapter = TransactionAdapter(filteredList,this@HomeFragment)
                btnSeeAll.setOnClickListener {
                    viewModel.seeAllClicked.postValue(true)
                }
            }
        }
    }

    override fun onClick(transaction: Transaction) {
        viewModel.drawerOpen.postValue(false)
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(transaction))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}