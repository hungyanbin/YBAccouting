package com.yanbin.ybaccouting.data

import com.yanbin.ybaccouting.Transaction
import kotlinx.coroutines.flow.*
import java.lang.RuntimeException

class RoomTransactionRepository(
    accountingDatabase: AccountingDatabase
) : TransactionRepository {

    private val dao = accountingDatabase.getTransactionDao()

    override fun getAll(): Flow<List<Transaction>> {
        return dao.getAll()
            .map { models ->
                models.map { model -> Transaction(model.total, model.deposit, model.withDraw, model.name) }
            }

    }

    override suspend fun add(transaction: Transaction) {
        return dao.addTransaction(
            TransactionModel().apply {
                this.withDraw = transaction.withDraw
                this.deposit = transaction.deposit
                this.total = transaction.total
                this.name = transaction.name
            }
        )
    }

    override fun getCurrentTotal(): Flow<Int> {
        return dao.getLastTransaction()
            .map { it?.total ?: 0 }
    }
}