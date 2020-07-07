package com.pritam.listapp.database

import android.util.Log
import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.utils.Constants

// Database cache which will call respective dao methods
class DatabaseCache(appDatabase: AppDatabase) {

    private val factDao = appDatabase.factDao()

    // insert facts in to database
    fun insert(facts: MutableList<Fact>) {
        Log.d(Constants.APP_TAG, "Inserting: ${facts.size} fact")
        factDao.insert(facts)
    }

    // get all facts from database
    fun getAllFacts(): MutableList<Fact> {
        return factDao.getAllFacts()
    }

    // delete all facts from database
    fun deleteAllFacts() {
        factDao.deleteAllFacts()
    }

    // get title from database
    fun getTitle(): String {
        return factDao.getTitle(Constants.APP_TITLE_ID)[0].title.toString()
    }

}
