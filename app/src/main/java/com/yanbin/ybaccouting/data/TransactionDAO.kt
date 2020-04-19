package com.yanbin.ybaccouting.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDAO {

    @Query("select * from transaction_model")
    suspend fun getAll(): List<TransactionModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transactionModel: TransactionModel)
}