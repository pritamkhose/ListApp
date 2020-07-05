package com.pritam.listapp.retrofit.rest

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pritam.listapp.database.DatabaseCache
import com.pritam.listapp.extensions.customPrefs
import com.pritam.listapp.extensions.get
import com.pritam.listapp.extensions.set
import com.pritam.listapp.retrofit.model.Facts
import com.pritam.listapp.utils.ConnectivityUtils
import com.pritam.listapp.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

open class ApiClient {
    private var apiInterface: ApiInterface? = null

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        apiInterface = retrofit.create(ApiInterface::class.java)
    }

    companion object {
        private var factRepository: ApiClient? = null

        @Synchronized
        @JvmStatic
        fun getInstance(): ApiClient {
            if (factRepository == null) {
                factRepository = ApiClient()
            }
            return factRepository!!
        }
    }

    // If network is available then load updated data from server and saved it in SQLite database.
    // If network is not available then will load data from SQLite database
    fun getFactList(context: Context, databaseCache: DatabaseCache): LiveData<Facts> {
        val data = MutableLiveData<Facts>()
        val prefs = customPrefs(context, Constants.APP_SHARED_PREFS)

        if (databaseCache.getAllFacts().isEmpty()) {
            // no data in database, load from server
            data.value = fetchFactsFromServer(data, databaseCache, context)
        } else {
            if (ConnectivityUtils.isNetworkAvailable(context)) {
                // data is present in db but network is present so will load updated data
                Log.d(Constants.APP_TAG, "get data response from Server: " + data)
                data.value = fetchFactsFromServer(data, databaseCache, context)
            } else {
                // data is present in db but network is not present so will data from db
                Log.d(Constants.APP_TAG, "get data response from database: " + data)
                data.value = Facts(prefs[Constants.PREFS_TITLE]!!, databaseCache.getAllFacts())
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
        val prefs = customPrefs(context, Constants.APP_SHARED_PREFS)
        apiInterface?.getFactLists()?.enqueue(object : Callback<Facts> {
            override fun onResponse(call: Call<Facts>, response: Response<Facts>) {
                Log.d(Constants.APP_TAG, "data response from server: " + response.body())
                data.value = response.body()

                // delete all facts from database and insert updated one
                databaseCache.deleteAllFacts()
                databaseCache.insert(response.body()?.rows?.toMutableList()!!)

                // add Toolbar in Preference
                prefs[Constants.PREFS_TITLE] = response.body()?.title

            }

            override fun onFailure(call: Call<Facts>, t: Throwable) {
                Log.d(Constants.APP_TAG, "Error While response : " + t.printStackTrace())
                data.value = null
            }
        })

        return data.value
    }
}

