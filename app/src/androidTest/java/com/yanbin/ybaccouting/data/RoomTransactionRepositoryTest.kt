package com.yanbin.ybaccouting.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.soywiz.klock.DateTime
import com.yanbin.ybaccouting.Transaction
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RoomTransactionRepositoryTest {

    private lateinit var database: AccountingDatabase
    private lateinit var repository: RoomTransactionRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
            AccountingDatabase::class.java).build()
        repository = RoomTransactionRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetAll() {
        runBlocking {
            repository.add(Transaction(
                total = 1000,
                withDraw = 40,
                deposit = 0,
                name = "lunch",
                recordTime = DateTime(2012, 4, 23, 12, 23, 59)
            ))

            val allTransactions = repository.getAll()
                .take(1)
                .toList().first()
            Assert.assertEquals(1, allTransactions.size)
            Assert.assertEquals("lunch", allTransactions[0].name)
            Assert.assertEquals(1000, allTransactions[0].total)
            Assert.assertEquals(40, allTransactions[0].withDraw)
            Assert.assertEquals(DateTime(2012, 4, 23, 12, 23, 59), allTransactions[0].recordTime)
        }
    }


    @Test
    fun getCurrentTotalFromLastTransaction() {
        runBlocking {
            repository.add(Transaction(
                total = 1000,
                withDraw = 40,
                deposit = 0,
                name = "lunch",
                recordTime = DateTime(2012, 4, 23, 12, 23, 59)
            ))
            repository.add(Transaction(
                total = 1900,
                withDraw = 0,
                deposit = 78,
                name = "dinner",
                recordTime = DateTime(2020, 4, 23, 12, 34, 56)
            ))

            val total = repository.getCurrentTotal()
            Assert.assertEquals(1900, total)
        }
    }

    @Test
    fun getCurrentTotalFromEmptyDataBaseShouldReturnZero() {
        runBlocking {
            val total = repository.getCurrentTotal()
            Assert.assertEquals(0, total)
        }
    }
}