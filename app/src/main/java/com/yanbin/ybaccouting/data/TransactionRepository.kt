package com.yanbin.ybaccouting.data

import com.yanbin.ybaccouting.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    fun getCurrentTotal(): Flow<Int>
    suspend fun add(transaction: Transaction)
}