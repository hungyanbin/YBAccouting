package com.yanbin.ybaccouting.data

import com.yanbin.ybaccouting.Transaction

class RoomTransactionRepository(
    accountingDatabase: AccountingDatabase
) : TransactionRepository {

    private val dao = accountingDatabase.getTransactionDao()

    override suspend fun getAll(): List<Transaction> {
        return dao.getAll()
            .map { model -> Transaction(model.total, model.deposit, model.withDraw, model.name) }
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
}