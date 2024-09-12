package com.example.composeskt.repository

import com.example.composeskt.data.models.CurrencyData
import retrofit2.Response
import retrofit2.http.Path

interface CurrencyRepository {
    suspend fun getCurrencyRates(@Path("currency") currencyCode: String): Response<CurrencyData>
}