package com.yanbin.ybaccouting.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(TransactionModel::class)], version = 1)
abstract class AccountingDatabase : RoomDatabase() {

    abstract fun getTransactionDao(): TransactionDAO

    companion object {
        const val DATABASE_NAME = "accouting_database"

        fun getDatabase(context: Context): AccountingDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AccountingDatabase::class.java, DATABASE_NAME
            ).build()
        }
    }
}