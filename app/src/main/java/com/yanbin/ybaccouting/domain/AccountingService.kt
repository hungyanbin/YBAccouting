package com.yanbin.ybaccouting.domain

import com.yanbin.ybaccouting.Transaction
import com.yanbin.ybaccouting.data.TransactionRepository

class AccountingService(
    private val transactionRepository: TransactionRepository
) {

    suspend fun getAllTransactions(): List<Transaction> {
        return transactionRepository.getAll()
    }

    suspend fun addWithdraw(name: String, amount: Int) {
        val total = transactionRepository.getCurrentTotal()
        val newTotal = total - amount
        transactionRepository.add(
            Transaction(total = newTotal, withDraw = amount, deposit = 0, name = name)
        )
    }

    suspend fun addDeposit(name: String, amount: Int) {
        val total = transactionRepository.getCurrentTotal()
        val newTotal = total + amount
        transactionRepository.add(
            Transaction(total = newTotal, withDraw = 0, deposit = amount, name = name)
        )
    }
}