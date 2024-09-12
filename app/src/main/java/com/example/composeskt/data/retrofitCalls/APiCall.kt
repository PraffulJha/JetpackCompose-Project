package com.example.composeskt.data.retrofitCalls

import com.example.composeskt.data.models.CurrencyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APiCall {

    @GET("currencies/{currency}.json")
    fun getCurrencyRates(@Path("currency") currencyCode: String): Call<CurrencyData>
}