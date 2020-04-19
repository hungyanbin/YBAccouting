package com.yanbin.ybaccouting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yanbin.ybaccouting.data.AccountingDatabase
import com.yanbin.ybaccouting.data.TransactionModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transactionAdapter = TransactionAdapter()
        recyclerTransaction.adapter = transactionAdapter
        recyclerTransaction.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        MainScope().launch {
            initDatabase()
            //loadData
            val transactions = getTransactions()

            withContext(Dispatchers.Main) {
                transactionAdapter.transactions.addAll(transactions)
                transactionAdapter.notifyDataSetChanged()
            }
        }
    }

    private suspend fun initDatabase() {
        withContext(Dispatchers.IO) {
            val db = AccountingDatabase.getDatabase(this@MainActivity)
            val transactionDao = db.getTransactionDao()
            transactionDao.addTransaction(TransactionModel().apply {
                name = "breakfast"
                total = 1000
                withDraw = 100
            })
        }
    }

    private suspend fun getTransactions(): List<Transaction> {
        return withContext(Dispatchers.IO) {
            val db = AccountingDatabase.getDatabase(this@MainActivity)
            val transactionDao = db.getTransactionDao()
            transactionDao.getAll()
                .map { model -> Transaction(model.total, model.deposit, model.withDraw, model.name) }
        }
    }

    private val fakeData = listOf(
        Transaction(total = 1000, deposit = 100, withDraw = 0, name = "breakfast"),
        Transaction(total = 1100, deposit = 100, withDraw = 0, name = "lunch"),
        Transaction(total = 1200, deposit = 100, withDraw = 0, name = "dinner")
    )
}
