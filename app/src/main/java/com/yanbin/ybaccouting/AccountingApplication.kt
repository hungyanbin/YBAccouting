package com.yanbin.ybaccouting

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin

class AccountingApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@AccountingApplication)
            modules(accountingModule, databaseModule)
        }
    }

}