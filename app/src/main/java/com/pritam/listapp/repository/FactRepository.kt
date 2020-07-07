package com.pritam.listapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pritam.listapp.database.DatabaseCache
import com.pritam.listapp.retrofit.model.Fact
import com.pritam.listapp.retrofit.model.Facts
import com.pritam.listapp.retrofit.rest.ApiClient
import com.pritam.listapp.utils.ConnectivityUtils
import com.pritam.listapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class FactRepository {

    companion object {
        private var factRepository: FactRepository? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): FactRepository {
            if (factRepository == null) {
                factRepository = FactRepository()
            }
            return factRepository!!
        }
    }

    // If network is available then load updated data from server and saved it in SQLite database.
    // If network is not available then will load data from SQLite database
    fun getFactList(context: Context, databaseCache: DatabaseCache): LiveData<Facts> {
        val data = MutableLiveData<Facts>()
        if (databaseCache.getAllFacts().isEmpty()) {
            // no data in database, load from server
            data.value = fetchFactsFromServer(data, databaseCache, context)
        } else {
            if (ConnectivityUtils.isNetworkAvailable(context)) {
                // data is present in db but network is present so will load updated data
                Log.d(Constants.APP_TAG, "get data response from Server: $data")
                data.value = fetchFactsFromServer(data, databaseCache, context)
            } else {
                // data is present in db but network is not present so will data from db
                Log.d(Constants.APP_TAG, "get data response from database: $data")
                data.value = Facts(databaseCache.getTitle(), removeTitleFromList(databaseCache.getAllFacts()))
            }

        }
        return data
    }

    // Load data from server and saved it in database, delete if any available in db
    private fun fetchFactsFromServer(
        data: MutableLiveData<Facts>,
        databaseCache: DatabaseCache,
        context: Context
    ): Facts? {
        ApiClient.instance.getFactLists().enqueue(object : Callback<Facts> {
            override fun onResponse(call: Call<Facts>, response: Response<Facts>) {
                Log.d(Constants.APP_TAG, "data response from server: " + response.body().toString())
                data.value = response.body()

                // delete all facts from database and insert updated one
                databaseCache.deleteAllFacts()
                val rows = response.body()?.rows?.toMutableList()!!
                // add Toolbar Title in Fact table
                rows.add(Fact(0, response.body()?.title, Constants.APP_TITLE_ID, null))
                Log.d(Constants.APP_TAG, rows.toString())
                databaseCache.insert(rows)
            }

            override fun onFailure(call: Call<Facts>, t: Throwable) {
                Log.d(Constants.APP_TAG, "Error While response : " + t.printStackTrace())
                data.value = null
            }
        })

        return data.value
    }

    private fun removeTitleFromList(rows: MutableList<Fact>): MutableList<Fact> {
        val mutableList = rows.toMutableList()
        for (iCount in rows.indices) {
            // remove item which has fact description APP_TITLE_ID
            if (rows[iCount].description != null && rows[iCount].description.equals(Constants.APP_TITLE_ID)) {
                mutableList.removeAt(iCount)
            }
        }
        return mutableList
    }
}

