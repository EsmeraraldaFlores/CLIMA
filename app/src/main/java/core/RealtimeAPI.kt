package com.example.clima
import com.example.proyectoshopifyka.model.ForecastResponse
import com.example.proyectoshopifyka.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RealtimeAPI {

    @GET("current.json")
    suspend fun getWeatherInfo(
        @Query("key") apiKey: String,
        @Query("q") location: String
    ): Response<Weather>

    @GET("forecast.json")
    suspend fun getForecastInfo(
        @Query("key") apiKey: String,
        @Query("q") location: String,
        @Query("days") days: Int = 7
    ): Response<ForecastResponse>
}
