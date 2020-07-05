package com.pritam.listapp.helpers

import android.content.Context
import com.pritam.listapp.database.AppDatabase
import com.pritam.listapp.database.DatabaseCache

// Dependency Injector for DatabaseCache
object Injector {

    fun provideCache(context: Context): DatabaseCache {
        val database: AppDatabase = AppDatabase.getDatabaseInstance(context)
        return DatabaseCache(database)
    }
}
