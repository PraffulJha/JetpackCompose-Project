package com.example.composeskt.data

import com.example.composeskt.data.retrofitCalls.APiCall
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@2024-03-06/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: APiCall by lazy {
        retrofit.create(APiCall::class.java)
    }
}