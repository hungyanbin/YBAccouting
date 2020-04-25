package com.yanbin.ybaccouting.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_model")
class TransactionModel{

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var total: Int = 0
    var deposit: Int = 0
    var withDraw: Int = 0
    @NonNull var dateTime: String = ""
    @NonNull var name: String = ""

    companion object {
        const val INVALID_DATETIME = "NaN"
    }
}