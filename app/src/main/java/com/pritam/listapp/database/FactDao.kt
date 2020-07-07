package com.pritam.listapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pritam.listapp.retrofit.model.Fact

@Dao
interface FactDao {

    // insert facts in database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(facts: MutableList<Fact>)

    // get all facts from database
    @Query("SELECT * FROM fact")
    fun getAllFacts(): MutableList<Fact>

    //delete all facts from database
    @Query("DELETE FROM fact")
    fun deleteAllFacts()

    // get all facts from database
    @Query("SELECT * FROM fact WHERE description  LIKE '%' || :search || '%'")
    fun getTitle(search: String?): MutableList<Fact>
}