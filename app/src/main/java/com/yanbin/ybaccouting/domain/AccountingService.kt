package com.yanbin.ybaccouting.domain

import com.yanbin.ybaccouting.Transaction
import com.yanbin.ybaccouting.data.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

@FlowPreview
@ExperimentalCoroutinesApi
class AccountingService(
    private val transactionRepository: TransactionRepository
) {

    fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionRepository.getAll()
    }

    suspend fun addWithdraw(name: String, amount: Int) {
        val currentTotal = transactionRepository.getCurrentTotal()
        val newTotal = currentTotal - amount

        transactionRepository.add(
            Transaction(total = newTotal, withDraw = amount, deposit = 0, name = name, recordTime = "TODO get current time")
        )
    }

    suspend fun addDeposit(name: String, amount: Int) {
        val currentTotal = transactionRepository.getCurrentTotal()
        val newTotal = currentTotal + amount

        transactionRepository.add(
            Transaction(total = newTotal, withDraw = 0, deposit = amount, name = name, recordTime = "TODO get current time")
        )
    }
}