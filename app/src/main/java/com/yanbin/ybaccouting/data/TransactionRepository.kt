package com.yanbin.ybaccouting.data

import com.yanbin.ybaccouting.Transaction

interface TransactionRepository {
    suspend fun getAll(): List<Transaction>
}