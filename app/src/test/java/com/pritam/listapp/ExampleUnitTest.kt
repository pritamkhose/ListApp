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

    private var listsFragment = ListsFragment()
    private var fact : Fact = Fact(1, "Beavers", "Beavers are second", "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg")
    private var factNull : Fact = Fact(2, null, null, null)
    private var factImgNull : Fact = Fact(3, "Eh",  "A chiefly Canadian", null)
    private var aList = mutableListOf<Fact>()

    @Before
    fun setUp() {
        aList.add(fact)
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
        assertEquals((listsFragment.removeNullItem(aList)).size, 1)
    }

    @Test
    fun removeNullItem_Test_factNull() {
        aList.clear()
        aList.add(factNull)
        System.out.println(aList.size.toString() + " --> "+ aList.toString())
        assertEquals((listsFragment.removeNullItem(aList)).size, 0)
    }

    @Test
    fun removeNullItem_Test_factImgNull() {
        aList.clear()
        aList.add(factImgNull)
        System.out.println(aList.size.toString() + " --> "+ aList.toString())
        assertEquals((listsFragment.removeNullItem(aList)).size, 1)
    }
}