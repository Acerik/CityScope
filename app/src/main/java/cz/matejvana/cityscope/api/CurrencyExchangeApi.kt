package cz.matejvana.cityscope.api

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyExchangeApi {

    @GET("v1/currencies/{currency}.json")
    suspend fun getCurrencyRates(@Path("currency") currency: String): Map<String, Any>
}