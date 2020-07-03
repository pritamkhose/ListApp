package com.pritam.listapp.retrofit.rest

import com.pritam.listapp.utils.Constants
import com.pritam.listapp.retrofit.model.Facts
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    // https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/facts.json
    @GET(Constants.FACT_URL)
    fun getFactLists(): Call<Facts>
}