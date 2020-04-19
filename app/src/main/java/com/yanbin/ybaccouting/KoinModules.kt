package com.yanbin.ybaccouting

import com.yanbin.ybaccouting.data.AccountingDatabase
import com.yanbin.ybaccouting.data.RoomTransactionRepository
import com.yanbin.ybaccouting.data.TransactionRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { AccountingDatabase.getDatabase(androidApplication()) }
}

val accountingModule = module {
    factory<TransactionRepository> {
        RoomTransactionRepository(get())
    }
}