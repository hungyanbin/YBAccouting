package com.yanbin.ybaccouting.data

import com.soywiz.klock.Date
import com.soywiz.klock.DateFormat
import com.soywiz.klock.DateTime
import com.yanbin.ybaccouting.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomTransactionRepository(
    accountingDatabase: AccountingDatabase
) : TransactionRepository {

    private val dao = accountingDatabase.getTransactionDao()

    override fun getAll(): Flow<List<Transaction>> {
        return dao.getAll()
            .map (::convertFromModel)
    }

    private suspend fun convertFromModel(models: List<TransactionModel>): List<Transaction> {
        return models.map { model ->
            val recordTime = if (model.dateTime == TransactionModel.INVALID_DATETIME) {
                DateTime.EPOCH
            } else {
                DateTime.parse(model.dateTime).local
            }

            Transaction(
                model.total,
                model.deposit,
                model.withDraw,
                model.name,
                recordTime
            )
        }
    }

    override fun getByDate(date: Date): Flow<List<Transaction>> {
        val dateString = "%${date.format(DateFormat.FORMAT_DATE)}%"
        return dao.getByDate(dateString)
            .map (::convertFromModel)
    }

    override suspend fun add(transaction: Transaction) {
        return dao.addTransaction(
            TransactionModel().apply {
                this.withDraw = transaction.withDraw
                this.deposit = transaction.deposit
                this.total = transaction.total
                this.name = transaction.name
                this.dateTime = transaction.recordTime.format(DateFormat.FORMAT1)
            }
        )
    }

    override suspend fun getCurrentTotal(): Int {
        return dao.getLastTransaction()?.total ?: 0
    }
}