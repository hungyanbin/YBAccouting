package com.yanbin.ybaccouting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter : RecyclerView.Adapter<TransationViewHolder>() {

    val transactions: MutableList<Transaction> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_transaction, parent, false)
        return TransationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransationViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }
}

class TransationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val textName = itemView.findViewById<AppCompatTextView>(R.id.textName)
    private val textMoneyDiff= itemView.findViewById<AppCompatTextView>(R.id.textMoneyDiff)
    private val textTotal = itemView.findViewById<AppCompatTextView>(R.id.textTotal)

    fun bind(transaction: Transaction) {
        textName.text = transaction.name
        textMoneyDiff.text = if (transaction.deposit > 0) {
            transaction.deposit.toString()
        } else {
            "-" + transaction.withDraw
        }
        textTotal.text = transaction.total.toString()
    }
}