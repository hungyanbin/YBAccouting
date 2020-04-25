package com.yanbin.ybaccouting.domain

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeProvider
import com.yanbin.ybaccouting.Transaction
import com.yanbin.ybaccouting.test
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AccountingServiceTest {

    private val fakeData = mutableListOf(
        Transaction(name = "breakfast", withDraw = 55, deposit = 0, total = 1000, recordTime = TimeProvider.now())
    )
    private val fakeNow = DateTime(2012, 3, 4, 12, 0)
    private val fakeTimeProvider = TimeProvider{fakeNow}

    @Test
    fun getAllTransactions() {
        runBlocking {
            val fakeRepository = FakeTransactionRepository(fakeData)
            val accountingService = AccountingService(fakeRepository, fakeTimeProvider)

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
            val accountingService = AccountingService(fakeRepository, fakeTimeProvider)
            val mockNow = DateTime(2012, 3, 4, 12, 0)

            accountingService.addWithdraw("lunch", 100)

            //assert
            assertThat(fakeRepository.lastedAddedTransaction)
                .isEqualTo(Transaction(total = 900, withDraw = 100, deposit = 0, name = "lunch", recordTime = mockNow))
        }
    }

    @Test
    fun `addDeposit (part time, 100) should add this transaction to repository`() {
        runBlocking {
            val fakeRepository = FakeTransactionRepository(currentTotal = 1000)
            val accountingService = AccountingService(fakeRepository, fakeTimeProvider)
            val mockNow = DateTime(2012, 3, 4, 12, 0)

            accountingService.addDeposit("part time", 100)

            //assert
            assertThat(fakeRepository.lastedAddedTransaction)
                .isEqualTo(Transaction(total = 1100, withDraw = 0, deposit = 100, name = "part time", recordTime = mockNow))
        }
    }
}