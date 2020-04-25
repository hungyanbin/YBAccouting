package com.yanbin.ybaccouting.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [(TransactionModel::class)], version = 2)
abstract class AccountingDatabase : RoomDatabase() {

    abstract fun getTransactionDao(): TransactionDAO

    companion object {
        private const val DATABASE_NAME = "accounting_database"

        fun getDatabase(context: Context): AccountingDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AccountingDatabase::class.java, DATABASE_NAME
            ).addMigrations(MIGRATION_1_2)
                .build()
        }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE transaction_model "
                    + "ADD COLUMN dateTime TEXT " +
                    "NOT NULL DEFAULT 'NaN'")
            }
        }
    }
}