package cz.matejvana.cityscope.repository

import cz.matejvana.cityscope.api.ApiResult
import cz.matejvana.cityscope.api.WeatherApi
import cz.matejvana.cityscope.api.WeatherResponse

class WeatherRepository(private val weatherApi: WeatherApi) {

    suspend fun getWeather(cityName: String): ApiResult<WeatherResponse> {
        return try {
            val response = weatherApi.getCurrentWeather(location = cityName)
            ApiResult.Success(response)
        } catch (e: Exception) {
            ApiResult.Error(e.message ?: "Unknown error")
        }
    }
}