package com.prathmesh.moneyx.ui.home.fragments

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.prathmesh.moneyx.R
import com.prathmesh.moneyx.comman.*
import com.prathmesh.moneyx.databinding.FragmentTransactionsBinding
import com.prathmesh.moneyx.ui.home.HomeViewModel
import com.prathmesh.moneyx.ui.home.Transaction
import com.prathmesh.moneyx.ui.home.adapters.TransactionAdapter
import com.prathmesh.moneyx.ui.home.adapters.TransactionDateAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TransactionsFragment : Fragment(R.layout.fragment_transactions), TransactionAdapter.OnClickListener {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding: FragmentTransactionsBinding
        get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private val month = MutableLiveData<String>()
    private val year = MutableLiveData<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTransactionsBinding.bind(view)

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        binding.btnMonth.text = calendar.getDisplayName(Calendar.MONTH,Calendar.LONG,Locale.getDefault())


        val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date()).split("-")
        month.postValue(date[1])
        year.postValue(date[2])

        val monthPopupMenu = PopupMenu(context,binding.btnMonth)
        monthPopupMenu.menu.add("January")
        monthPopupMenu.menu.add("February")
        monthPopupMenu.menu.add("March")
        monthPopupMenu.menu.add("April")
        monthPopupMenu.menu.add("May")
        monthPopupMenu.menu.add("June")
        monthPopupMenu.menu.add("July")
        monthPopupMenu.menu.add("August")
        monthPopupMenu.menu.add("September")
        monthPopupMenu.menu.add("October")
        monthPopupMenu.menu.add("November")
        monthPopupMenu.menu.add("December")

        monthPopupMenu.setOnMenuItemClickListener {
            month.postValue(it.title.toMonthInt())
            true
        }

        val yearPopupMenu = PopupMenu(context,binding.btnYear)
        yearPopupMenu.menu.add("2020")
        yearPopupMenu.menu.add("2021")
        yearPopupMenu.menu.add("2022")
        yearPopupMenu.menu.add("2023")
        yearPopupMenu.menu.add("2024")

        yearPopupMenu.setOnMenuItemClickListener {
            year.postValue(it.title.toString())
            true
        }

        month.observe(viewLifecycleOwner){
            binding.btnMonth.text = it.getMonthInWords()
            getTransactions()
        }
        year.observe(viewLifecycleOwner){
            binding.btnYear.text = it
            getTransactions()
        }

        binding.apply {

            btnMonth.setOnClickListener {
                monthPopupMenu.show()
            }

            btnYear.setOnClickListener { yearPopupMenu.show() }
        }

    }

    private fun getTransactions() {
        if (month.value.isNullOrEmpty() || year.value.isNullOrEmpty())
            return
        viewModel.transactions.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.apply {
                        if (it.data!!.isNotEmpty()) {
                            binding.tvNotFound.isVisible = false
                            binding.rvTransactions.isVisible = true
                            val y = it.data.filter { it.date.isCorrectYear(year.value.toString()) }
                            val f = y.filter { it.date.isCorrectMonth(month.value.toString()) }
                            val data = f.groupBy { t -> t.date }
                            val keys = data.keys.toMutableList()
                            keys.sortByDescending { d -> d.dateToMillis() }
                            rvTransactions.layoutManager = LinearLayoutManager(requireContext())
                            rvTransactions.adapter = TransactionDateAdapter(
                                data,
                                keys,
                                getDate(System.currentTimeMillis()),
                                getDate(System.currentTimeMillis() - 86400000),
                                this@TransactionsFragment
                            )
                            if (data.isEmpty()){
                                tvNoRecordFound.visibility = View.VISIBLE
                            }else
                                tvNoRecordFound.visibility = View.GONE
                        } else {
                            binding.tvNotFound.isVisible = true
                            binding.rvTransactions.isVisible = false
                        }
                    }
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun String.isCorrectMonth(m: String): Boolean{
        val x = this.split("-")[1]
        return x == m
    }

    private fun String.isCorrectYear(y: String): Boolean{
        val x = this.split("-")[2]
        return x == y
    }

    private fun getDate(timeInMillis: Long): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(timeInMillis))
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