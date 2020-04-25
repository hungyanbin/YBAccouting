package com.yanbin.ybaccouting.data

import com.yanbin.ybaccouting.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAll(): Flow<List<Transaction>>
    suspend fun getCurrentTotal(): Int
    suspend fun add(transaction: Transaction)
}