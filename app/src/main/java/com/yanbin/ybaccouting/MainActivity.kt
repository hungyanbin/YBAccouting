package com.yanbin.ybaccouting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transactionAdapter = TransactionAdapter()
        recyclerTransaction.adapter = transactionAdapter
        recyclerTransaction.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerTransaction.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        btnAddTransaction.setOnClickListener {
            AddTransactionDialog().show(supportFragmentManager, "")
        }

        homeViewModel.allTransactions.observe(this, Observer { transactions ->
            transactionAdapter.transactions.clear()
            transactionAdapter.transactions.addAll(transactions)
            transactionAdapter.notifyDataSetChanged()
        })

        homeViewModel.currentTotal.observe(this, Observer { total ->
            textTotal.text = total
        })

        calendar.setDaySelectedListener { date ->
            Log.i("testt", "date: $date")
        }

    }

}
