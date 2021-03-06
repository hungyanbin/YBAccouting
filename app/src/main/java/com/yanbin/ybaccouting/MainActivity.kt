package com.yanbin.ybaccouting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

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

        homeViewModel.currentDate.observe(this, Observer { date ->
            calendar.toDate(date)
        })

        homeViewModel.title.observe(this, Observer {
            toolbar.title = it
        })

        homeViewModel.transactionDates.observe(this, Observer {
            calendar.updateBadges(it)
        })

        calendar.setDaySelectedListener { date ->
            homeViewModel.selectDate(date)
        }

    }

}
