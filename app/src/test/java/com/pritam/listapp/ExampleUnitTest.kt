package com.pritam.listapp

import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.ui.fragment.ListsFragment
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    var listsFragment = ListsFragment()
    var fact : Fact = Fact("Beavers", "Beavers are second", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg")
    var factNull : Fact = Fact(null, null, null)
    var factImgNull : Fact = Fact("Eh",  "A chiefly Canadian", null)
    var aList = mutableListOf<Fact>()

    @Before
    fun setUp() {
        aList.add(fact)
        aList.add(factNull)
        aList.add(factImgNull)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun addition_isNotCorrect() {
        assertNotEquals(4, 2 + 1)
    }

    @Test
    fun removeNullItem_Test() {
        System.out.println(aList.size.toString() + " --> "+ aList.toString())
        assertEquals((listsFragment.removeNullItem(aList)).size, 2)
        aList.removeAt(0)
        System.out.println(aList.size.toString() + " --> "+ aList.toString())
        assertEquals((listsFragment.removeNullItem(aList)).size, 1)
        aList.removeAt(1)
        System.out.println(aList.size.toString() + " --> "+ aList.toString())
        assertEquals((listsFragment.removeNullItem(aList)).size, 0)
    }
}