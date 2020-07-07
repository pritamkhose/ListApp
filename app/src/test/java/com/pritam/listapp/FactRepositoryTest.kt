package com.pritam.listapp

import com.pritam.listapp.repository.FactRepository
import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.utils.Constants.Companion.APP_TITLE_ID
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FactRepositoryUnitTest {

    private var factRepository = FactRepository()
    private var fact1: Fact = Fact(1, "About Canada", APP_TITLE_ID, null)
    private var fact2: Fact = Fact(2, null, null, null)
    private var fact3: Fact = Fact(3, "Eh", "A chiefly Canadian", null)
    private var aList = mutableListOf<Fact>()

    @Before
    fun setUp() {
        aList.add(fact1)
        aList.add(fact2)
        aList.add(fact3)
    }

    @Test
    fun removeAppTitle_Test() {
        println(aList.size.toString() + " --> " + aList.toString())
        aList = factRepository.removeTitleFromList(aList)
        Assert.assertEquals(aList.size, 2)
        println(aList.size.toString() + " --> " + aList.toString())
    }

}