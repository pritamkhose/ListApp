package com.pritam.listapp.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.pritam.listapp.database.AppDatabase
import com.pritam.listapp.database.FactDao
import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.utils.Constants
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.runner.RunWith
import kotlinx.coroutines.runBlocking
import org.junit.*

@RunWith(AndroidJUnit4::class)
class FactsDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var factDao: FactDao
    private var aList = mutableListOf<Fact>()

    @Before
    fun setUp() {
        val fact1 = Fact(1, "About Canada", Constants.APP_TITLE_ID, null)
        val fact2 = Fact(3, "Eh", "A chiefly Canadian", null)
        aList.add(fact1)
        aList.add(fact2)
    }

    @Before fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        factDao = database.factDao()
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun testGetAllFacts() {
        // Insert test data
        factDao.insert(aList)
        val aList = factDao.getAllFacts()
        assertThat(aList.size, equalTo(2))
    }

    @Test fun testGetTitle() {
        // Insert test data
        factDao.insert(aList)
        // Get app title from db data
        val title = factDao.getTitle(Constants.APP_TITLE_ID)[0].title.toString()
        println(title)
        Assert.assertEquals("About Canada", title)
    }

    @Test fun testDeleteAllFacts() {
        // Insert test data
        factDao.insert(aList)
        assertThat(factDao.getAllFacts().size, equalTo(2))
        // Delete test data
        factDao.deleteAllFacts()
        assertThat(factDao.getAllFacts().size, equalTo(0))
    }

}