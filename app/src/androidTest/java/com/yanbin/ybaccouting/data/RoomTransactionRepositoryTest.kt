package com.yanbin.ybaccouting.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
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

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().context,
            AccountingDatabase::class.java).build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndGetAll() {
        runBlocking {
            val dao = database.getTransactionDao()

            dao.addTransaction(TransactionModel().apply {
                total = 1000
                withDraw = 40
                name = "lunch"
            })

            val allTransactions = dao.getAll()
                .take(1)
                .toList().first()
            Assert.assertEquals(1, allTransactions.size)
            Assert.assertEquals(1L, allTransactions[0].id)
            Assert.assertEquals("lunch", allTransactions[0].name)
            Assert.assertEquals(1000, allTransactions[0].total)
            Assert.assertEquals(40, allTransactions[0].withDraw)
        }
    }

    @Test
    fun insertAndGetLastTransaction() {
        runBlocking {
            val dao = database.getTransactionDao()

            dao.addTransaction(TransactionModel().apply {
                total = 1000
                withDraw = 40
                name = "lunch"
            })

            val lastTransaction = dao.getLastTransaction()
            Assert.assertNotNull(lastTransaction)
            Assert.assertEquals(1L, lastTransaction!!.id)
            Assert.assertEquals("lunch", lastTransaction!!.name)
            Assert.assertEquals(1000, lastTransaction!!.total)
            Assert.assertEquals(40, lastTransaction!!.withDraw)
        }
    }
}