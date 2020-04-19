package com.yanbin.ybaccouting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transactionAdapter = TransactionAdapter()
        recyclerTransaction.adapter = transactionAdapter
        recyclerTransaction.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        transactionAdapter.transactions.addAll(fakeData)
        transactionAdapter.notifyDataSetChanged()

    }

    private val fakeData = listOf(
        Transaction(total = 1000, deposit = 100, withDraw = 0, name = "breakfast"),
        Transaction(total = 1100, deposit = 100, withDraw = 0, name = "lunch"),
        Transaction(total = 1200, deposit = 100, withDraw = 0, name = "dinner")
    )
}
