package com.yanbin.ybaccouting.domain

import com.soywiz.klock.Date
import com.yanbin.ybaccouting.Transaction
import com.yanbin.ybaccouting.data.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTransactionRepository(
    private val fakeData: MutableList<Transaction> = mutableListOf(),
    private val currentTotal: Int = 0
) : TransactionRepository {

    var lastedAddedTransaction: Transaction? = null

    override fun getAll(): Flow<List<Transaction>> {
        return flowOf(fakeData)
    }

    override fun getByDate(date: Date): Flow<List<Transaction>> {
        return flowOf(fakeData)
    }

    override suspend fun getCurrentTotal(): Int {
        return currentTotal
    }

    override suspend fun add(transaction: Transaction) {
        fakeData.add(transaction)
        lastedAddedTransaction = transaction
    }


}