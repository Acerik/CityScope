package cz.matejvana.cityscope.repository

import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.api.CurrencyExchangeApi

data class CurrencyRate(
    val code: String,
    val value: Double
)

class CurrencyExchangeRepository(private val api: CurrencyExchangeApi) {

    suspend fun getCurrencyRates(currency: String): ApiResult<List<CurrencyRate>> {
        return try {
            val response = api.getCurrencyRates(currency)
            val rates = transformResponse(response, currency)
            ApiResult.Success(rates)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }

    private fun transformResponse(response: Map<String, Any>, currency: String): List<CurrencyRate> {
        val eurRates = response[currency] as? Map<String, Double> ?: emptyMap()
        return eurRates.map { (code, value) ->
            CurrencyRate(code, value)
        }
    }
}