package com.example.composeskt.repository

import com.example.composeskt.data.models.CurrencyData
import com.example.composeskt.data.retrofitCalls.APiCall
import retrofit2.Response
import retrofit2.awaitResponse

class CurrencyRepositoryImpl(private val aPiCall: APiCall) : CurrencyRepository {
    override suspend fun getCurrencyRates(currencyCode: String): Response<CurrencyData> {
        return aPiCall.getCurrencyRates(currencyCode).awaitResponse()
    }
}