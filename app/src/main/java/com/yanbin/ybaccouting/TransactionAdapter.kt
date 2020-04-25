package com.yanbin.ybaccouting

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter : RecyclerView.Adapter<TransactionViewHolder>() {

    val transactions: MutableList<Transaction> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }
}

class TransactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val textName = itemView.findViewById<AppCompatTextView>(R.id.textName)
    private val textMoneyDiff= itemView.findViewById<AppCompatTextView>(R.id.textMoneyDiff)
    private val textTime = itemView.findViewById<AppCompatTextView>(R.id.textTime)

    fun bind(transaction: Transaction) {
        textName.text = transaction.name
        textMoneyDiff.text = if (transaction.deposit > 0) {
            "+" + transaction.deposit.toString()
        } else {
            "-" + transaction.withDraw
        }
        textTime.text = transaction.recordTime
    }
}