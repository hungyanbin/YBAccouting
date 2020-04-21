package com.yanbin.ybaccouting.domain

import com.yanbin.ybaccouting.Transaction
import com.yanbin.ybaccouting.test
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AccountingServiceTest {

    private val fakeData = mutableListOf(
        Transaction(name = "breakfast", withDraw = 55, deposit = 0, total = 1000)
    )

    @Test
    fun getAllTransactions() {
        runBlocking {
            val fakeRepository = FakeTransactionRepository(fakeData)
            val accountingService = AccountingService(fakeRepository)

            //act & assert
            accountingService.getAllTransactions()
                .test(this)
                .assertValues(fakeData)
        }
    }

    @Test
    fun `addWithdraw (lunch, 100) should add this transaction to repository`() {
        runBlocking {
            val fakeRepository = FakeTransactionRepository(currentTotal = 1000)
            val accountingService = AccountingService(fakeRepository)

            accountingService.addWithdraw("lunch", 100)
                .collect {  }

            //assert
            assertThat(fakeRepository.lastedAddedTransaction)
                .isEqualTo(Transaction(total = 900, withDraw = 100, deposit = 0, name = "lunch"))
        }
    }

    @Test
    fun `addDeposit (part time, 100) should add this transaction to repository`() {
        runBlocking {
            val fakeRepository = FakeTransactionRepository(currentTotal = 1000)
            val accountingService = AccountingService(fakeRepository)

            accountingService.addDeposit("part time", 100)
                .collect {  }

            //assert
            assertThat(fakeRepository.lastedAddedTransaction)
                .isEqualTo(Transaction(total = 1100, withDraw = 0, deposit = 100, name = "part time"))
        }
    }
}