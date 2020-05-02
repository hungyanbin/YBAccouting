package com.yanbin.ybaccouting

import androidx.lifecycle.*
import com.soywiz.klock.Date
import com.soywiz.klock.TimeProvider
import com.yanbin.ybaccouting.domain.AccountingService
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val accountingService: AccountingService,
    private val timeProvider: TimeProvider
): ViewModel() {

    private val dateChannel = ConflatedBroadcastChannel<Date>()

    val allTransactions: LiveData<List<Transaction>> =  dateChannel.asFlow()
        .flatMapLatest { date ->
            accountingService.getTransactionsByDate(date)
        }.asLiveData()
    val currentTotal = MutableLiveData<String>()
    val currentDate = MutableLiveData<Date>()

    init {
        viewModelScope.launch {
            accountingService.getAllTransactions()
                .collect { transactions ->
                    if (transactions.isEmpty()) {
                        return@collect
                    }
                    val lastTransaction = transactions.last()
                    currentTotal.postValue(mapMoneyFormat(lastTransaction.total))
                }
        }

        dateChannel.offer(timeProvider.now().date)
        currentDate.postValue(timeProvider.now().date)
    }

    fun selectDate(date: Date) {
        dateChannel.offer(date)
    }

    private fun mapMoneyFormat(money: Int): String {
        return "$ " + money.toString()
            .chunked(3)
            .reduce { init, aac ->
            "$init,$aac"
        }
    }
}