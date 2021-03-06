package com.yanbin.ybaccouting

import com.soywiz.klock.TimeProvider
import com.yanbin.ybaccouting.data.AccountingDatabase
import com.yanbin.ybaccouting.data.RoomTransactionRepository
import com.yanbin.ybaccouting.data.TransactionRepository
import com.yanbin.ybaccouting.domain.AccountingService
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single { AccountingDatabase.getDatabase(androidApplication()) }
}

val accountingModule = module {
    viewModel { HomeViewModel(get(), TimeProvider) }

    factory<TransactionRepository> {
        RoomTransactionRepository(get())
    }

    single { AccountingService(get(), TimeProvider) }
}