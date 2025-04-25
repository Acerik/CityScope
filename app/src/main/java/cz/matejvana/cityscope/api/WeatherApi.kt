package cz.matejvana.cityscope.api

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface WeatherApi {

    //https://app.swaggerhub.com/apis-docs/WeatherAPI.com/WeatherAPI/1.0.2#/APIs/realtime-weather
    @GET("/v1/current.json")
    suspend fun getCurrentWeather(
        //@Query("key") apiKey: String = ApiKeys.WEATHER_API_KEY,
        @Query("q") location: String,
        @Query("lang") lang: String = Locale.getDefault().language
    ): WeatherResponse
}