package com.yanbin.ybaccouting.data

import com.soywiz.klock.Date
import com.yanbin.ybaccouting.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    fun getByDate(date: Date): Flow<List<Transaction>>
    suspend fun getCurrentTotal(): Int
    suspend fun add(transaction: Transaction)
}