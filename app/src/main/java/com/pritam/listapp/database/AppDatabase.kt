package com.pritam.listapp.database

import android.content.Context
import com.pritam.listapp.retrofit.model.Fact
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//  Room database for this app
@Database(entities = [(Fact::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // define dao interface
    abstract fun factDao(): FactDao

    // database access object
    companion object {
        private var databaseName = "AppDB.db"
        private var INSTANCE: AppDatabase? = null

        // Get database instance is not null else create new
        fun getDatabaseInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(context,
                    AppDatabase::class.java,
                    databaseName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }.also { INSTANCE = it }
    }
}