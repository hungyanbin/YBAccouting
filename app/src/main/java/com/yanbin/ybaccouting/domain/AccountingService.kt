package com.yanbin.ybaccouting.domain

import com.soywiz.klock.TimeProvider
import com.yanbin.ybaccouting.Transaction
import com.yanbin.ybaccouting.data.TransactionRepository
import kotlinx.coroutines.flow.Flow


class AccountingService(
    private val transactionRepository: TransactionRepository,
    private val timeProvider: TimeProvider
) {

    fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionRepository.getAll()
    }

    suspend fun addWithdraw(name: String, amount: Int) {
        val currentTotal = transactionRepository.getCurrentTotal()
        val newTotal = currentTotal - amount

        transactionRepository.add(
            Transaction(total = newTotal, withDraw = amount, deposit = 0, name = name, recordTime = timeProvider.now())
        )
    }

    suspend fun addDeposit(name: String, amount: Int) {
        val currentTotal = transactionRepository.getCurrentTotal()
        val newTotal = currentTotal + amount

        transactionRepository.add(
            Transaction(total = newTotal, withDraw = 0, deposit = amount, name = name, recordTime = timeProvider.now())
        )
    }
}