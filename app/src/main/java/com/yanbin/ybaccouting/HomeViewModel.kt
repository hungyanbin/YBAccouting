package com.yanbin.ybaccouting

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.ybaccouting.domain.AccountingService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val accountingService: AccountingService
): ViewModel() {

    val allTransactions = MutableLiveData<List<Transaction>>()
    val currentTotal = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            accountingService.getAllTransactions()
                .collect { transactions ->
                    allTransactions.postValue(transactions)
                    val lastTransaction = transactions.last()
                    currentTotal.postValue(mapMoneyFormat(lastTransaction.total))
                }
        }
    }

    private fun mapMoneyFormat(money: Int): String {
        return "$ " + money.toString()
            .chunked(3)
            .reduce { init, aac ->
            "$init,$aac"
        }
    }
}