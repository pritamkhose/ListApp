package com.pritam.listapp

import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.retrofit.model.Facts

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class FactsTest {

    private lateinit var fact: Fact
    private lateinit var facts: Facts

    @Before
    fun setUp() {
        fact = Fact(
            1,
            "Beavers",
            "Beavers are second",
            "http://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/American_Beaver.jpg/220px-American_Beaver.jpg"
        )
        facts = Facts("About Canada", null, "No Data Found")
    }

    @Test
    fun test_default_values_fact() {
        assertEquals("Beavers", fact.title)
        assertEquals("Beavers are second", fact.description)
    }

    @Test
    fun test_default_values_facts() {
        assertEquals("About Canada", facts.title)
        assertEquals("No Data Found", facts.error)
    }

}