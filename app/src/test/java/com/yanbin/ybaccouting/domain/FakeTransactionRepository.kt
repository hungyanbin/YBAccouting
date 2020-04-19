package com.yanbin.ybaccouting.domain

import com.yanbin.ybaccouting.Transaction
import com.yanbin.ybaccouting.data.TransactionRepository

class FakeTransactionRepository(
    private val fakeData: MutableList<Transaction> = mutableListOf(),
    private val currentTotal: Int = 0
) : TransactionRepository {
    override suspend fun getAll(): List<Transaction> {
        return fakeData
    }

    override suspend fun add(transaction: Transaction) {
        fakeData.add(transaction)
    }

    override suspend fun getCurrentTotal(): Int {
        return currentTotal
    }
}