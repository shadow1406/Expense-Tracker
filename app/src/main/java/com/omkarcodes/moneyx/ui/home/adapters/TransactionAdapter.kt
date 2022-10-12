package com.omkarcodes.moneyx.ui.home.adapters

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.omkarcodes.moneyx.R
import com.omkarcodes.moneyx.comman.Constants
import com.omkarcodes.moneyx.databinding.ItemTransactionBinding
import com.omkarcodes.moneyx.ui.home.Transaction
import kotlin.math.log

class TransactionAdapter(
    private val list: List<Transaction>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(private val binding: ItemTransactionBinding)
        : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(position: Int){
            val transaction = list[position]
            val category =
                if (transaction.type == "income") Constants.incomeCategory.find { it.id == transaction.categoryId }
            else Constants.expenseCategory.find { it.id == transaction.categoryId }
            category?.let {
                binding.apply {
                    cardview.setCardBackgroundColor(Color.parseColor(it.bg))
                    ivLogo.setImageResource(it.icon)
                    ivLogo.imageTintList = ColorStateList.valueOf(Color.parseColor(it.color))
                    tvTitle.text = it.title
                    tvSubtitle.text = transaction.description
                    tvTime.text = transaction.time
                    if (transaction.type == "income"){
                        tvAmount.setTextColor(ContextCompat.getColor(root.context, R.color.incomeGreen))
                        tvAmount.text = "+ ₹${transaction.amount}"
                    }else{
                        tvAmount.setTextColor(ContextCompat.getColor(root.context, R.color.expenseRed))
                        tvAmount.text = "- ₹${transaction.amount}"
                    }
                    root.setOnClickListener { listener.onClick(transaction) }
                }
            }
        }
    }

    interface OnClickListener{
        fun onClick(transaction: Transaction)
    }
}