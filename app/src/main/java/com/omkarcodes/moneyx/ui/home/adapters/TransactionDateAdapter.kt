package com.omkarcodes.moneyx.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omkarcodes.moneyx.comman.toDateInMillis
import com.omkarcodes.moneyx.databinding.ItemTransactionGroupBinding
import com.omkarcodes.moneyx.ui.home.Transaction

class TransactionDateAdapter(
    val data: Map<String,List<Transaction>>,
    val keys: List<String>,
    val today: String,
    val yesterday: String,
    private val listener: TransactionAdapter.OnClickListener
) : RecyclerView.Adapter<TransactionDateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTransactionGroupBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(private val binding: ItemTransactionGroupBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            val key = keys[position]
            val list = data[key]
            binding.apply {
                tvDate.text = if (key == today) "Today" else if (key == yesterday) "Yesterday" else key
                val orderedList = list!!.sortedByDescending { t -> (t.date+ " "+t.time).toDateInMillis() }
                rvTransactions.layoutManager = LinearLayoutManager(root.context)
                rvTransactions.adapter = TransactionAdapter(orderedList,listener)
            }

        }
    }
}