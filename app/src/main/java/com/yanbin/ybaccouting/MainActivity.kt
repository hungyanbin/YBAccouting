package com.yanbin.ybaccouting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanbin.ybaccouting.domain.AccountingService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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
            val service = get<AccountingService>()
            service.getAllTransactions()
                .collect { transactions ->
                    transactionAdapter.transactions.clear()
                    transactionAdapter.transactions.addAll(transactions)
                    transactionAdapter.notifyDataSetChanged() }
        }
    }

}
