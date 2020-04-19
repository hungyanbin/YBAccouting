package com.yanbin.ybaccouting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanbin.ybaccouting.domain.AccountingService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transactionAdapter = TransactionAdapter()
        recyclerTransaction.adapter = transactionAdapter
        recyclerTransaction.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        btnAddTransaction.setOnClickListener {
            AddTransactionDialog().show(supportFragmentManager, "")
        }

        MainScope().launch {
            val transactions = getTransactions()

            withContext(Dispatchers.Main) {
                transactionAdapter.transactions.addAll(transactions)
                transactionAdapter.notifyDataSetChanged()
            }
        }
    }

    private suspend fun getTransactions(): List<Transaction> {
        return withContext(Dispatchers.IO) {
            val service = get<AccountingService>()
            service.getAllTransactions()
        }
    }
}
